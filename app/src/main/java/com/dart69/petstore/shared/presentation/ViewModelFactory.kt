package com.dart69.petstore.shared.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.avatar.domain.usecases.LoadNextUseCase
import com.dart69.petstore.avatar.domain.usecases.LoadPreviousUseCase
import com.dart69.petstore.avatar.presentation.AvatarViewModel
import com.dart69.petstore.details.domain.usecases.DownloadAvatarUseCase
import com.dart69.petstore.details.domain.usecases.SendMessageUseCase
import com.dart69.petstore.details.domain.usecases.UpdatePetUseCase
import com.dart69.petstore.details.presentation.DetailsViewModel
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.home.presentation.HomeViewModel
import com.dart69.petstore.main.domain.usecases.ObserveMessagesUseCase
import com.dart69.petstore.main.presentation.MainViewModel
import com.dart69.petstore.shared.model.AvailableDispatchers
import kotlinx.coroutines.CoroutineScope

class ViewModelFactory(
    private val availableDispatchers: AvailableDispatchers,
    private val getPetItemsUseCase: GetPetsSortedByFavouriteUseCase,
    private val getSelectionDetailsUseCase: GetSelectionDetailsUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val togglePetSelectedUseCase: TogglePetSelectedUseCase,
    private val toggleAllPetsSelectedUseCase: ToggleAllPetsSelectedUseCase,
    private val toggleSelectedItemsToFavouriteUseCase: ToggleSelectedPetsFavouriteUseCase,
    private val deleteSelectedPetsUseCase: DeleteSelectedPetsUseCase,
    private val togglePetFavouriteUseCase: TogglePetFavouriteUseCase,
    private val applicationScope: CoroutineScope,
    private val refreshRepositoryUseCase: RefreshRepositoryUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val downloadAvatarUseCase: DownloadAvatarUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadPreviousUseCase: LoadPreviousUseCase,
    private val loadNextUseCase: LoadNextUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> MainViewModel(observeMessagesUseCase)
            HomeViewModel::class.java -> HomeViewModel(
                availableDispatchers,
                getPetItemsUseCase,
                getSelectionDetailsUseCase,
                deletePetUseCase,
                togglePetSelectedUseCase,
                togglePetFavouriteUseCase,
                toggleAllPetsSelectedUseCase,
                deleteSelectedPetsUseCase,
                toggleSelectedItemsToFavouriteUseCase,
                refreshRepositoryUseCase,
                applicationScope,
            )
            DetailsViewModel::class.java -> DetailsViewModel(
                deletePetUseCase,
                applicationScope,
                availableDispatchers,
                updatePetUseCase,
                downloadAvatarUseCase,
                sendMessageUseCase,
            )
            AvatarViewModel::class.java -> AvatarViewModel(
                availableDispatchers,
                loadPreviousUseCase,
                loadNextUseCase,
            )
            else -> error("Can't find actual viewModel.")
        }
        return viewModel as T
    }
}