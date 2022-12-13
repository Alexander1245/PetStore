package com.dart69.petstore.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.dart69.petstore.R
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.shared.model.*
import com.dart69.petstore.shared.then
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dispatchers: AvailableDispatchers,
    getPetsSortedByFavouriteUseCase: GetPetsSortedByFavouriteUseCase,
    getSelectionDetailsUseCase: GetSelectionDetailsUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val togglePetSelectedUseCase: TogglePetSelectedUseCase,
    private val togglePetFavourite: TogglePetFavouriteUseCase,
    private val toggleAllPetsSelectedUseCase: ToggleAllPetsSelectedUseCase,
    private val deleteSelectedPetsUseCase: DeleteSelectedPetsUseCase,
    private val toggleSelectedPetsFavouriteUseCase: ToggleSelectedPetsFavouriteUseCase,
    private val refreshRepositoryUseCase: RefreshRepositoryUseCase,
    private val applicationScope: CoroutineScope
) : ViewModel(), PetAdapterCallbacks {
    private val mutableNavigationDestination = MutableSharedFlow<NavDirections>()
    val navigationDestination = mutableNavigationDestination.asSharedFlow()

    val pets = getPetsSortedByFavouriteUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Task.Loading())

    val selectionDetails = getSelectionDetailsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SelectionDetails.EMPTY)

    val isGroupActionsVisible = pets.combine(selectionDetails) { task, details ->
        task.isCompleted() && details.hasSelected()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val actionHint = selectionDetails.map { details ->
        if (details.allSelected()) R.string.unselect_all else R.string.select_all
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), R.string.select_all)

    init {
        refreshRepository()
    }

    override fun onItemViewClick(item: SelectablePet) {
        if (selectionDetails.value.hasSelected()) {
            togglePetSelectedUseCase(item)
        } else {
            openPetDetails(item)
        }
    }

    override fun onItemViewLongClick(item: SelectablePet): Boolean {
        togglePetSelectedUseCase(item)
        return true
    }

    override fun onFavouriteClick(item: SelectablePet) {
        applicationScope.launch(dispatchers.io) {
            togglePetFavourite(item)
        }
    }

    override fun onAvatarClick(item: SelectablePet) {
        viewModelScope.launch(dispatchers.default) {
            mutableNavigationDestination.emit(
                HomeFragmentDirections.actionHomeFragmentToAvatarFragment(item)
            )
        }
    }

    override fun onPopupMenuItemsClick(item: SelectablePet, itemId: Int): Boolean = when (itemId) {
        R.id.itemDelete -> onDeleteClick(item) then true
        R.id.itemToggleFavourite -> onFavouriteClick(item) then true
        R.id.itemToggleSelected -> togglePetSelectedUseCase(item) then true
        else -> false
    }

    override fun onDeleteClick(item: SelectablePet) {
        applicationScope.launch(dispatchers.io) {
            deletePetUseCase(item)
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

    private fun toggleSelectedItemsFavourite() {
        applicationScope.launch(dispatchers.io) {
            toggleSelectedPetsFavouriteUseCase()
        }
    }

    private fun deleteSelectedItems() {
        applicationScope.launch(dispatchers.io) {
            deleteSelectedPetsUseCase()
        }
    }

    private fun refreshRepository() {
        applicationScope.launch(dispatchers.io) {
            refreshRepositoryUseCase()
        }
    }

    private fun openPetDetails(item: SelectablePet) {
        viewModelScope.launch(dispatchers.default) {
            mutableNavigationDestination.emit(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }
    }
}