package com.dart69.petstore.presentation.home.recyclerview

import androidx.annotation.IdRes
import com.dart69.petstore.model.item.Item
import com.dart69.petstore.model.item.ListItem

interface ItemCallbacks {
    fun interface ItemViewClickListener<T> {
        fun onItemViewClick(item: T)
    }

    fun interface ItemViewLongClickListener<T> {
        fun onItemViewLongClick(item: T): Boolean
    }

    fun interface FavouriteClickListener<T : Item.CanBeFavourite> {
        fun onFavouriteClick(item: T)
    }

    fun interface AvatarClickListener<T : Item.HasAvatar> {
        fun onAvatarClick(item: T)
    }

    fun interface MoreClickListener<T> {
        fun onPopupMenuItemsClick(item: T, @IdRes itemId: Int): Boolean
    }

    fun interface DeleteClickListener<K, T : Item.Unique<K>> {
        fun onDeleteClick(item: T)
    }

    interface PetCallbacks<K, T : ListItem<K>> : ItemViewClickListener<T>,
        ItemViewLongClickListener<T>, AvatarClickListener<T>,
        FavouriteClickListener<T>, MoreClickListener<T>, DeleteClickListener<K, T>
}