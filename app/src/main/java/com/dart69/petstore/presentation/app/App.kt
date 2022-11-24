package com.dart69.petstore.presentation.app

import android.app.Application
import com.dart69.petstore.data.repository.FakeRepository
import com.dart69.petstore.data.repository.FavouriteItemRepository
import com.dart69.petstore.model.item.Pet
import com.dart69.petstore.presentation.home.recyclerview.PetItem
import com.dart69.petstore.presentation.utils.ImageLoader
import com.dart69.petstore.presentation.utils.ResourceManager
import com.dart69.petstore.presentation.utils.selection.Tracker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class App : Application() {
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    fun provideDataCount(): Int = 150

    fun provideSelectionTracker(): Tracker<Long, PetItem> = Tracker.Implementation()

    fun provideResourceManager(): ResourceManager = ResourceManager.Implementation(this)

    fun provideImageLoader(): ImageLoader = ImageLoader.GlideImplementation(this)

    fun provideRepository(): FavouriteItemRepository<Long, Pet> =
        FakeRepository(provideDataCount(), provideDispatcher())
}