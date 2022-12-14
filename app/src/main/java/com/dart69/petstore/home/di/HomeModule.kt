package com.dart69.petstore.home.di

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.domain.PetsSelectionTracker
import com.dart69.petstore.home.domain.usecases.*
import com.dart69.petstore.shared.model.AvailableDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    fun provideDeletePetUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker
    ): DeletePetUseCase =
        DeletePetUseCase.Implementation(repository, tracker)

    @Provides
    fun provideDeleteSelectedPetsUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker
    ): DeleteSelectedPetsUseCase =
        DeleteSelectedPetsUseCase.Implementation(repository, tracker)

    @Provides
    fun provideGetPetsSortedByFavouriteUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker,
        dispatchers: AvailableDispatchers
    ): GetPetsSortedByFavouriteUseCase =
        GetPetsSortedByFavouriteUseCase.Implementation(repository, tracker, dispatchers)

    @Provides
    fun provideGetSelectionDetailsUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker,
        dispatchers: AvailableDispatchers
    ): GetSelectionDetailsUseCase =
        GetSelectionDetailsUseCase.Implementation(repository, tracker, dispatchers)

    @Provides
    fun provideRefreshRepositoryUseCase(
        repository: PetsRepository
    ): RefreshRepositoryUseCase = RefreshRepositoryUseCase.Implementation(repository)

    @Provides
    fun provideTogglePetSelectedUseCase(
        selectionTracker: PetsSelectionTracker
    ): TogglePetSelectedUseCase = TogglePetSelectedUseCase.Implementation(selectionTracker)

    @Provides
    fun provideToggleAllPetsSelectedUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker
    ): ToggleAllPetsSelectedUseCase =
        ToggleAllPetsSelectedUseCase.Implementation(repository, tracker)

    @Provides
    fun provideTogglePetFavouriteUseCase(
        repository: PetsRepository
    ): TogglePetFavouriteUseCase = TogglePetFavouriteUseCase.Implementation(repository)

    @Provides
    fun provideToggleSelectedPetsFavouriteUseCase(
        repository: PetsRepository,
        tracker: PetsSelectionTracker
    ): ToggleSelectedPetsFavouriteUseCase =
        ToggleSelectedPetsFavouriteUseCase.Implementation(repository, tracker)

    @Provides
    fun provideClearSelectionUseCase(
        tracker: PetsSelectionTracker
    ): ClearSelectionUseCase = ClearSelectionUseCase.Implementation(tracker)
}