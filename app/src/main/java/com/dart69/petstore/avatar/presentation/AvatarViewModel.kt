package com.dart69.petstore.avatar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dart69.petstore.avatar.domain.usecases.LoadNextUseCase
import com.dart69.petstore.avatar.domain.usecases.LoadPreviousUseCase
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.model.AvailableDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val dispatchers: AvailableDispatchers,
    private val loadPreviousUseCase: LoadPreviousUseCase,
    private val loadNextUseCase: LoadNextUseCase,
) : ViewModel() {
    private val isPreviousEnabled = MutableStateFlow(true)
    private val isNextEnabled = MutableStateFlow(true)
    private val currentPet = MutableStateFlow<SelectablePet?>(null)

    init {
        viewModelScope.launch(dispatchers.default) {
            currentPet.onEach { current ->
                val previousPet = if(current == null) null else loadPreviousUseCase(current)
                val nextPet = if(current == null) null else loadNextUseCase(current)
                isPreviousEnabled.emit(previousPet != null)
                isNextEnabled.emit(nextPet != null)
            }.collect()
        }
    }

    val screenState = currentPet.combine(isPreviousEnabled) { pet, isPreviousEnabled ->
        requireNotNull(pet) { "You must initialize ViewModel before observing screenState." }
        ScreenState(
            title = pet.name,
            avatarUri = pet.avatarUri,
            isPreviousEnabled = isPreviousEnabled,
        )
    }.combine(isNextEnabled) { state, isNextEnabled ->
        state.copy(isNextEnabled = isNextEnabled)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ScreenState.INITIAL)

    fun initialize(pet: SelectablePet) {
        if (currentPet.value != null) return
        viewModelScope.launch(dispatchers.default) {
            currentPet.emit(pet)
        }
    }

    fun loadNext() {
        loadPet { loadNextUseCase(currentPet.value!!) }
    }

    fun loadPrevious() {
        loadPet { loadPreviousUseCase(currentPet.value!!) }
    }

    private fun loadPet(block: suspend () -> SelectablePet?) {
        viewModelScope.launch(dispatchers.default) {
            currentPet.emit(block())
        }
    }

    data class ScreenState(
        val title: String = "",
        val avatarUri: String = "",
        val isPreviousEnabled: Boolean = true,
        val isNextEnabled: Boolean = true,
        val isInProgress: Boolean = true,
    ) {
        companion object {
            val INITIAL = ScreenState()
        }
    }
}