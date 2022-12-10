package com.dart69.petstore.shared.presentation

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.dart69.petstore.R
import com.dart69.petstore.details.domain.usecases.DownloadAvatarUseCase
import com.dart69.petstore.details.domain.usecases.SendMessageUseCase
import com.dart69.petstore.details.domain.usecases.UpdatePetUseCase
import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.data.asPetsRepository
import com.dart69.petstore.home.model.*
import com.dart69.petstore.home.model.usecases.*
import com.dart69.petstore.main.domain.usecases.ObserveMessagesUseCase
import com.dart69.petstore.shared.data.repository.FakeRepository
import com.dart69.petstore.shared.data.repository.imageSources
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.ItemSelectionTracker
import com.dart69.petstore.shared.model.Logger
import com.dart69.petstore.shared.model.MessageObserver
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope

fun Fragment.provideNavOptions(): NavOptions =
    (requireContext().applicationContext as App).provideDefaultNavOptions()

class App : Application() {
    private val faker = Faker()
    private val dataCount = 150
    private val data = List(dataCount) {
        val cat = faker.cat()
        Pet(
            it.toLong(), cat.name(), cat.breed() + "\n" + cat.registry(), false,
            imageSources[it % imageSources.size]
        ).makeSelectable()
        //TEST ONLY
    }
    private val logger = Logger.Default(CoroutineScope(Dispatchers.Default))
    private val repository = FakeRepository(data, provideAvailableDispatchers()).asPetsRepository()
    private val selectionTracker =
        ItemSelectionTracker<Long, SelectablePet>(logger).asPetsSelectionTracker()
    private val imageLoader: ImageLoader by lazy { GlideImplementation(this) }
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_up)
        .setExitAnim(R.anim.slide_down)
        .setPopEnterAnim(R.anim.slide_up)
        .setPopExitAnim(R.anim.slide_down)
        .build()

    private val messageObserver =
        MessageObserver.Implementation(provideApplicationScope(), provideAvailableDispatchers())

    fun provideDefaultNavOptions(): NavOptions = navOptions

    fun provideRepository(): PetsRepository = repository

    fun provideAvailableDispatchers(): AvailableDispatchers = ViewModelDispatchers()

    fun provideImageLoader(): ImageLoader = imageLoader

    /**
     * Must be in ViewModel component. In other words, selectionTracker must be unique for each
     * selectable screen in application. With Hilt: @InstallIn(ViewModelComponent::class)
     * */
    fun provideSelectionTracker(): PetsSelectionTracker = selectionTracker

    fun provideGetPetItemsUseCase(): GetPetsSortedByFavouriteUseCase =
        GetPetsSortedByFavouriteUseCase.Implementation(
            provideRepository(),
            provideSelectionTracker(),
            provideAvailableDispatchers()
        )

    fun provideGetSelectionDetailsUseCase(): GetSelectionDetailsUseCase =
        GetSelectionDetailsUseCase.Implementation(
            provideRepository(),
            provideSelectionTracker(),
            provideAvailableDispatchers()
        )

    fun provideDeleteSinglePetUseCase(): DeletePetUseCase =
        DeletePetUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSingleItemSelectedUseCase(): TogglePetSelectedUseCase =
        TogglePetSelectedUseCase.Implementation(provideSelectionTracker())

    fun provideToggleAllPetsSelectedUseCase(): ToggleAllPetsSelectedUseCase =
        ToggleAllPetsSelectedUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSelectedItemsToFavouriteUseCase(): ToggleSelectedPetsFavouriteUseCase =
        ToggleSelectedPetsFavouriteUseCase.Implementation(
            provideRepository(),
            provideSelectionTracker()
        )

    fun provideDeleteSelectedPetsUseCase(): DeleteSelectedPetsUseCase =
        DeleteSelectedPetsUseCase.Implementation(provideRepository(), provideSelectionTracker())

    fun provideToggleSingleItemFavouriteUseCase(): TogglePetFavouriteUseCase =
        TogglePetFavouriteUseCase.Implementation(provideRepository())

    fun provideApplicationScope(): CoroutineScope = MainScope()

    fun provideRefreshRepositoryUseCase(): RefreshRepositoryUseCase =
        RefreshRepositoryUseCase.Implementation(provideRepository())

    fun provideUpdatePetUseCase(): UpdatePetUseCase =
        UpdatePetUseCase.Implementation(provideRepository())

    fun provideDownloadAvatarUseCase(): DownloadAvatarUseCase =
        DownloadAvatarUseCase.Implementation(provideImageLoader())

    fun provideObserveMessageUseCase(): ObserveMessagesUseCase =
        ObserveMessagesUseCase.Implementation(messageObserver)

    fun provideSendMessageUseCase(): SendMessageUseCase =
        SendMessageUseCase.Implementation(messageObserver)
}