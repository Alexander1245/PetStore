package com.dart69.petstore.shared.di

import android.content.Context
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.data.DataSource
import com.dart69.petstore.shared.data.repository.FakeRepository
import com.dart69.petstore.shared.data.repository.ItemRepository
import com.dart69.petstore.shared.model.*
import com.dart69.petstore.shared.presentation.ApplicationDispatchers
import com.dart69.petstore.shared.presentation.GlideImplementation
import com.dart69.petstore.shared.presentation.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedSingletonModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = MainScope()

    @Provides
    @Singleton
    fun provideAvailableDispatchers(): AvailableDispatchers = ApplicationDispatchers()

    @Provides
    @Singleton
    fun provideLogger(
        applicationScope: CoroutineScope
    ): Logger = Logger.Default(applicationScope)

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader = GlideImplementation(context)

    @Provides
    @Singleton
    fun provideMessageObserver(
        applicationScope: CoroutineScope,
        dispatchers: AvailableDispatchers
    ): MessageObserver = MessageObserver.Implementation(applicationScope, dispatchers)

    @Provides
    fun provideBaseRepository(
        dataSource: DataSource<Long, SelectablePet>,
        dispatchers: AvailableDispatchers
    ): ItemRepository<Long, SelectablePet> =
        FakeRepository(dataSource, dispatchers)

    @Provides
    fun provideBaseTracker(logger: Logger): SelectionTracker<Long, SelectablePet> =
        ItemSelectionTracker(logger)
}