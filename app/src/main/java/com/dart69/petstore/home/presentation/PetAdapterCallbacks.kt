package com.dart69.petstore.home.presentation

import androidx.annotation.IdRes
import com.dart69.petstore.shared.presentation.recyclerview.ItemCallbacks

interface PetAdapterCallbacks : ItemCallbacks.ItemViewClickListener<PetItem>,
    ItemCallbacks.ItemViewLongClickListener<PetItem> {
    fun onFavouriteClick(item: PetItem)

    fun onAvatarClick(item: PetItem)

    fun onPopupMenuItemsClick(item: PetItem, @IdRes itemId: Int): Boolean

    fun onDeleteClick(item: PetItem)
}