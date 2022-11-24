package com.dart69.petstore.presentation.extensions

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.presentation.app.App
import com.dart69.petstore.presentation.utils.ImageLoader
import com.dart69.petstore.presentation.utils.ViewModelFactory

fun Context.provideFactory(): ViewModelProvider.Factory {
    val app = applicationContext as App
    return ViewModelFactory(
        app.provideRepository(),
        app.provideSelectionTracker(),
        app.provideResourceManager()
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