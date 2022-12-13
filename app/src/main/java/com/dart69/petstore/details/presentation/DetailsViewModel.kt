package com.dart69.petstore.details.presentation

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dart69.petstore.R
import com.dart69.petstore.details.domain.usecases.DownloadAvatarUseCase
import com.dart69.petstore.details.domain.usecases.SendMessageUseCase
import com.dart69.petstore.details.domain.usecases.UpdatePetUseCase
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.home.model.usecases.DeletePetUseCase
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.item.toggleFavourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val uncheckedIcon = R.drawable.ic_baseline_favourite_unchecked
private const val checkedIcon = R.drawable.ic_baseline_favorite_checked

class DetailsViewModel(
    private val deletePetUseCase: DeletePetUseCase,
    private val applicationScope: CoroutineScope,
    private val dispatchers: AvailableDispatchers,
    private val updatePetUseCase: UpdatePetUseCase,
    private val downloadAvatarUseCase: DownloadAvatarUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val isReadyToClose = MutableStateFlow(false)
    private val isToggleEnabled = MutableStateFlow(false)
    private val currentPet = MutableStateFlow<SelectablePet?>(null)
    private val toggleIconResource = isToggleEnabled.map { isEnabled ->
        if (isEnabled) checkedIcon else uncheckedIcon
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), uncheckedIcon)

    val screenState = currentPet.combine(toggleIconResource) { pet, iconRes ->
        checkNotNull(pet) { "ViewModel must be initialized before observing screenState." }
        ScreenState(pet.name, pet.details, pet.avatarUri, iconRes)
    }.combine(isReadyToClose) { screenState, isReadyToClose ->
        screenState.copy(isReadyToClose = isReadyToClose)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ScreenState.INITIAL)

    override fun onCleared() {
        updatePetIfToggleChanged()
    }

    fun initialize(pet: SelectablePet) {
        if(currentPet.value != null) return
        viewModelScope.launch(dispatchers.default) {
            currentPet.emit(pet)
            isToggleEnabled.emit(pet.isFavourite)
        }
    }

    fun onMenuItemsClick(@IdRes id: Int): Boolean = when (id) {
        R.id.itemDelete -> deletePet()
        R.id.itemToggleFavourite -> onToggleFavouriteClick()
        R.id.itemDownloadAvatar -> downloadAvatar()
        else -> false
    }

    private fun updatePetIfToggleChanged() {
        val isPetToggleNotChanged = isToggleEnabled.value == currentPet.value?.isFavourite
        if (isPetToggleNotChanged) return
        applicationScope.launch(dispatchers.io) {
            val pet = currentPet.value?.toggleFavourite() ?: return@launch
            updatePetUseCase(pet)
        }
    }

    private fun deletePet(): Boolean {
        viewModelScope.launch(dispatchers.default) {
            isReadyToClose.emit(true)
        }
        applicationScope.launch(dispatchers.io) {
            currentPet.value?.let { pet -> deletePetUseCase(pet) }
        }
        return true
    }

    private fun onToggleFavouriteClick(): Boolean {
        isToggleEnabled.update { isEnabled -> !isEnabled }
        return true
    }

    private fun downloadAvatar(): Boolean {
        try {
            downloadAvatarUseCase(currentPet.value!!.avatarUri)
        } catch (exception: Exception) {
            sendMessageUseCase(exception.message.orEmpty())
        }
        return true
    }

    data class ScreenState(
        val titleText: String = "",
        val detailsText: String = "",
        val avatarUri: String = "",
        val iconResource: Int = uncheckedIcon,
        val isReadyToClose: Boolean = false
    ) {
        companion object {
            val INITIAL = ScreenState()
        }
    }
}