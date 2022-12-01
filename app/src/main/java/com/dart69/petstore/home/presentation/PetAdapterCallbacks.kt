package com.dart69.petstore.home.presentation

import androidx.annotation.IdRes
import com.dart69.petstore.shared.presentation.recyclerview.AdapterCallbacks

interface PetAdapterCallbacks : AdapterCallbacks.ItemViewClickListener<SelectablePet>,
    AdapterCallbacks.ItemViewLongClickListener<SelectablePet> {
    fun onFavouriteClick(item: SelectablePet)

    fun onAvatarClick(item: SelectablePet)

    fun onPopupMenuItemsClick(item: SelectablePet, @IdRes itemId: Int): Boolean

    fun onDeleteClick(item: SelectablePet)
}