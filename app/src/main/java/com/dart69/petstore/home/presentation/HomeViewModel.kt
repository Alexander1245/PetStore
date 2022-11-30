package com.dart69.petstore.home.presentation

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dart69.petstore.R
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.SelectionDetails
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.map
import com.dart69.petstore.shared.presentation.MessageSender
import com.dart69.petstore.shared.presentation.ResourceManager
import com.dart69.petstore.shared.then
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dispatchers: AvailableDispatchers,
    getPetsUseCase: GetPetsUseCase,
    getSelectionDetailsUseCase: GetSelectionDetailsUseCase,
    getActionHintUseCase: GetActionHintUseCase,
    getGroupActionsVisibilityUseCase: GetGroupActionsVisibilityUseCase,
    private val deleteSinglePetUseCase: DeleteSinglePetUseCase,
    private val toggleSinglePetSelectedUseCase: ToggleSinglePetSelectedUseCase,
    private val toggleAllPetsSelectedUseCase: ToggleAllPetsSelectedUseCase,
    private val deleteSelectedPetsUseCase: DeleteSelectedPetsUseCase,
    private val toggleSelectedItemsToFavouriteUseCase: ToggleSelectedItemsToFavouriteUseCase,
    private val toggleSinglePetFavourite: ToggleSinglePetFavourite,
    resourceManager: ResourceManager,
) : ViewModel(), PetAdapterCallbacks, UiHandler, MessageSender, ResourceManager by resourceManager {
    private val mutableMessages = MutableSharedFlow<String>()
    val messages = mutableMessages.asSharedFlow()

    val pets = getPetsUseCase()
        .map { task -> task.map { pets -> pets.sortedByDescending { it.isFavourite } } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Task.Loading())

    val selectionDetails = getSelectionDetailsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SelectionDetails.EMPTY)

    val groupActionsVisible = getGroupActionsVisibilityUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), View.INVISIBLE)

    val actionHint = getActionHintUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), getString(R.string.select_all))

    override fun sendMessage(messageRes: Int) {
        viewModelScope.launch(dispatchers.main) {
            mutableMessages.emit(getString(messageRes))
        }
    }

    override fun sendMessage(message: String) {
        viewModelScope.launch(dispatchers.main) {
            mutableMessages.emit(message)
        }
    }

    override fun onItemViewClick(item: PetItem) {
        if (selectionDetails.value.selected > 0) {
            toggleSinglePetSelectedUseCase(item)
        } else {
            sendMessage(R.string.item_view_clicked)
        }
    }

    override fun onItemViewLongClick(item: PetItem): Boolean {
        toggleSinglePetSelectedUseCase(item)
        return true
    }

    override fun onFavouriteClick(item: PetItem) {
        viewModelScope.launch(dispatchers.main) {
            toggleSinglePetFavourite(item)
        }
    }

    override fun onAvatarClick(item: PetItem) {
        sendMessage(R.string.not_yet_implemented)
    }

    override fun onPopupMenuItemsClick(item: PetItem, itemId: Int): Boolean = when (itemId) {
        R.id.itemDelete -> onDeleteClick(item) then true
        R.id.itemToggleFavourite -> onFavouriteClick(item) then true
        R.id.itemToggleSelected -> toggleSinglePetSelectedUseCase(item) then true
        else -> false
    }

    override fun onDeleteClick(item: PetItem) {
        viewModelScope.launch(dispatchers.main) {
            deleteSinglePetUseCase(item)
        }
    }

    override fun onGroupActionItemClick(id: Int): Boolean = when (id) {
        R.id.itemToggleFavouriteForSelected -> toggleSelectedItemsFavourite() then true
        R.id.itemDeleteSelected -> deleteSelectedItems() then true
        else -> false
    }

    override fun onActionButtonClick() {
        toggleAllPetsSelectedUseCase()
    }

    private fun toggleSelectedItemsFavourite() {
        viewModelScope.launch(dispatchers.main) {
            toggleSelectedItemsToFavouriteUseCase()
        }
    }

    private fun deleteSelectedItems() {
        viewModelScope.launch(dispatchers.main) {
            deleteSelectedPetsUseCase()
        }
    }
}