package com.dart69.petstore.shared.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.home.presentation.HomeViewModel

class ViewModelFactory(
    private val resourceManager: ResourceManager,
    private val availableDispatchers: AvailableDispatchers,
    private val getPetItemsUseCase: GetPetsUseCase,
    private val getSelectionDetailsUseCase: GetSelectionDetailsUseCase,
    private val getActionHintUseCase: GetActionHintUseCase,
    private val showGroupActionsUseCase: GetGroupActionsVisibilityUseCase,
    private val deleteSinglePetUseCase: DeleteSinglePetUseCase,
    private val toggleSingleItemSelectedUseCase: ToggleSinglePetSelectedUseCase,
    private val toggleAllPetsSelectedUseCase: ToggleAllPetsSelectedUseCase,
    private val toggleSelectedItemsToFavouriteUseCase: ToggleSelectedItemsToFavouriteUseCase,
    private val deleteSelectedPetsUseCase: DeleteSelectedPetsUseCase,
    private val toggleSinglePetFavourite: ToggleSinglePetFavourite,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(
                availableDispatchers,
                getPetItemsUseCase,
                getSelectionDetailsUseCase,
                getActionHintUseCase,
                showGroupActionsUseCase,
                deleteSinglePetUseCase,
                toggleSingleItemSelectedUseCase,
                toggleAllPetsSelectedUseCase,
                deleteSelectedPetsUseCase,
                toggleSelectedItemsToFavouriteUseCase,
                toggleSinglePetFavourite,
                resourceManager
            )
            else -> error("Can't find actual viewModel.")
        }
        return viewModel as T
    }
}