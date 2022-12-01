package com.dart69.petstore.home.presentation

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dart69.petstore.R
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.SelectionDetails
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.then
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dispatchers: AvailableDispatchers,
    getPetsSortedByFavouriteUseCase: GetPetsSortedByFavouriteUseCase,
    getSelectionDetailsUseCase: GetSelectionDetailsUseCase,
    private val deleteSinglePetUseCase: DeleteSinglePetUseCase,
    private val toggleSinglePetSelectedUseCase: ToggleSinglePetSelectedUseCase,
    private val toggleSinglePetFavourite: ToggleSinglePetFavouriteUseCase,
    private val toggleAllPetsSelectedUseCase: ToggleAllPetsSelectedUseCase,
    private val deleteSelectedPetsUseCase: DeleteSelectedPetsUseCase,
    private val toggleSelectedItemsToFavouriteUseCase: ToggleSelectedItemsToFavouriteUseCase,
) : ViewModel(), PetAdapterCallbacks {
    private val mutableMessages = MutableSharedFlow<Int>()
    val messages = mutableMessages.asSharedFlow()

    val pets = getPetsSortedByFavouriteUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Task.Loading())

    val selectionDetails = getSelectionDetailsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SelectionDetails.EMPTY)

    val groupActionsVisibility = pets.combine(selectionDetails) { task, details ->
        if (details.selected > 0 && task is Task.Completed) View.VISIBLE else View.GONE
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), View.GONE)

    val actionHint = selectionDetails.map { details ->
        if (details.selected == details.total) R.string.unselect_all else R.string.select_all
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), R.string.select_all)

    override fun onItemViewClick(item: SelectablePet) {
        if (selectionDetails.value.selected > 0) {
            toggleSinglePetSelectedUseCase(item)
        } else {
            sendMessage(R.string.item_view_clicked)
        }
    }

    override fun onItemViewLongClick(item: SelectablePet): Boolean {
        toggleSinglePetSelectedUseCase(item)
        return true
    }

    override fun onFavouriteClick(item: SelectablePet) {
        viewModelScope.launch(dispatchers.main) {
            toggleSinglePetFavourite(item)
        }
    }

    override fun onAvatarClick(item: SelectablePet) {
        sendMessage(R.string.not_yet_implemented)
    }

    override fun onPopupMenuItemsClick(item: SelectablePet, itemId: Int): Boolean = when (itemId) {
        R.id.itemDelete -> onDeleteClick(item) then true
        R.id.itemToggleFavourite -> onFavouriteClick(item) then true
        R.id.itemToggleSelected -> toggleSinglePetSelectedUseCase(item) then true
        else -> false
    }

    override fun onDeleteClick(item: SelectablePet) {
        viewModelScope.launch(dispatchers.main) {
            deleteSinglePetUseCase(item)
        }
    }

    fun onGroupActionItemClick(id: Int): Boolean = when (id) {
        R.id.itemToggleFavouriteForSelected -> toggleSelectedItemsFavourite() then true
        R.id.itemDeleteSelected -> deleteSelectedItems() then true
        else -> false
    }

    fun onActionButtonClick() {
        toggleAllPetsSelectedUseCase()
    }

    fun sendMessage(@StringRes messageRes: Int) {
        viewModelScope.launch(dispatchers.main) {
            mutableMessages.emit(messageRes)
        }
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