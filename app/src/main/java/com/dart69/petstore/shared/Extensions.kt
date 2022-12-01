package com.dart69.petstore.shared

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.shared.presentation.App
import com.dart69.petstore.shared.presentation.ImageLoader
import com.dart69.petstore.shared.presentation.ViewModelFactory

fun <T> T.use(block: T.() -> Unit) = block(this)

infix fun <T, R> T.then(right: R): R = right

fun Context.provideFactory(): ViewModelProvider.Factory {
    val app = applicationContext as App
    return ViewModelFactory(
        app.provideAvailableDispatchers(),
        app.provideGetPetItemsUseCase(),
        app.provideGetSelectionDetailsUseCase(),
        app.provideDeleteSinglePetUseCase(),
        app.provideToggleSingleItemSelectedUseCase(),
        app.provideToggleAllPetsSelectedUseCase(),
        app.provideToggleSelectedItemsToFavouriteUseCase(),
        app.provideDeleteSelectedPetsUseCase(),
        app.provideToggleSingleItemFavouriteUseCase()
    )
}

fun Fragment.provideFactory(): ViewModelProvider.Factory =
    requireContext().provideFactory()

fun Fragment.provideImageLoader(): ImageLoader =
    (requireContext().applicationContext as App).provideImageLoader()

fun View.showPopupMenu(@MenuRes menuRes: Int, itemClickListener: (Int) -> Boolean) {
    PopupMenu(context, this).apply {
        inflate(menuRes)
        setOnMenuItemClickListener { itemClickListener(it.itemId) }
    }.show()
}