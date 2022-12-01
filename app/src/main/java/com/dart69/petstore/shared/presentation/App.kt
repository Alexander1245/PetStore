package com.dart69.petstore.shared.presentation

import android.app.Application
import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.data.asPetsRepository
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.model.asPetsSelectionTracker
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.shared.data.repository.FakeRepository
import com.dart69.petstore.shared.data.repository.imageSources
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.ItemSelectionTracker
import com.dart69.petstore.shared.model.Logger
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    private val dataCount = 150
    private val data = List(dataCount) {
        Pet(
            it.toLong(), Faker().cat().name(), Faker().cat().breed(), false,
            imageSources[it % imageSources.size]
        )
        //TEST ONLY
    }
    private val logger = Logger.Default(CoroutineScope(Dispatchers.Default))
    private val repository = FakeRepository(data, provideAvailableDispatchers()).asPetsRepository()
    private val selectionTracker = ItemSelectionTracker<Long, Pet>(logger).asPetsSelectionTracker()
    private lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        imageLoader = ImageLoader.GlideImplementation(this)
        CoroutineScope(Dispatchers.Default).launch {
            repository.initialize()
        }
    }

    fun provideRepository(): PetsRepository = repository

    fun provideAvailableDispatchers(): AvailableDispatchers = ViewModelDispatchers()

    fun provideImageLoader(): ImageLoader = imageLoader

    fun provideSelectionTracker(): PetsSelectionTracker = selectionTracker

    fun provideGetPetItemsUseCase(): GetPetsSortedByFavouriteUseCase =
        GetPetsSortedByFavouriteUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideGetSelectionDetailsUseCase(): GetSelectionDetailsUseCase =
        GetSelectionDetailsUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideDeleteSinglePetUseCase(): DeleteSinglePetUseCase =
        DeleteSinglePetUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSingleItemSelectedUseCase(): ToggleSinglePetSelectedUseCase =
        ToggleSinglePetSelectedUseCase.Implementation(provideSelectionTracker())

    fun provideToggleAllPetsSelectedUseCase(): ToggleAllPetsSelectedUseCase =
        ToggleAllPetsSelectedUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSelectedItemsToFavouriteUseCase(): ToggleSelectedItemsToFavouriteUseCase =
        ToggleSelectedItemsToFavouriteUseCase.Implementation(
            provideRepository(),
            provideSelectionTracker()
        )

    fun provideDeleteSelectedPetsUseCase(): DeleteSelectedPetsUseCase =
        DeleteSelectedPetsUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSingleItemFavouriteUseCase(): ToggleSinglePetFavouriteUseCase =
        ToggleSinglePetFavouriteUseCase.Implementation(provideRepository())
}