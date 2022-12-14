package com.dart69.petstore.home.di

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.data.asPetsRepository
import com.dart69.petstore.home.domain.*
import com.dart69.petstore.home.domain.model.Pet
import com.dart69.petstore.home.domain.model.SelectablePet
import com.dart69.petstore.home.domain.model.makeSelectable
import com.dart69.petstore.shared.data.DataSource
import com.dart69.petstore.shared.data.FakeLocalDataSource
import com.dart69.petstore.shared.data.repository.ItemRepository
import com.dart69.petstore.shared.data.repository.imageSources
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.SelectionTracker
import com.github.javafaker.Faker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeSingletonModule {

    @Provides
    @Singleton
    fun provideFaker(): Faker =
        Faker()

    @Provides
    @Singleton
    fun provideDataCount(): Int = 150

    @Provides
    @Singleton
    fun provideDataSource(
        dataCount: Int,
        faker: Faker,
        dispatchers: AvailableDispatchers,
    ): DataSource<Long, SelectablePet> =
        FakeLocalDataSource(List(dataCount) {
            val cat = faker.cat()
            Pet(
                id = it.toLong(),
                name = cat.name(),
                details = cat.breed() + "\n" + cat.registry(),
                isFavourite = false,
                avatarUri = imageSources[it % imageSources.size]
            ).makeSelectable()
        }, dispatchers)

    @Provides
    @Singleton
    fun provideTracker(baseTracker: SelectionTracker<Long, SelectablePet>): PetsSelectionTracker =
        baseTracker.asPetsSelectionTracker()

    @Provides
    @Singleton
    fun providePetsRepository(
        baseRepository: ItemRepository<Long, SelectablePet>
    ): PetsRepository = baseRepository.asPetsRepository()
}