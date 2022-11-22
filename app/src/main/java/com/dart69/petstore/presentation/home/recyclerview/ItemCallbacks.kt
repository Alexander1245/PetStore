package com.dart69.petstore.presentation.home.recyclerview

import android.view.View
import com.dart69.petstore.model.item.Item
import com.dart69.petstore.model.item.ListItem
import com.dart69.petstore.presentation.utils.selection.ItemHighlighter

interface ItemCallbacks {
    fun interface ItemViewClickListener<T> {
        fun onItemViewClick(item: T, position: Int)
    }

    fun interface ItemViewLongClickListener<T> {
        fun onItemViewLongClick(item: T, position: Int): Boolean
    }

    fun interface FavouriteClickListener<T : Item.CanBeFavourite> {
        fun onFavouriteClick(item: T, position: Int)
    }

    fun interface AvatarClickListener<T : Item.HasAvatar> {
        fun onAvatarClick(item: T, position: Int)
    }

    fun interface MoreClickListener<T> {
        fun onMoreClick(item: T, position: Int, parent: View)
    }

    fun interface DeleteClickListener<K, T : Item.Unique<K>> {
        fun onDeleteClick(item: T, position: Int)
    }

    interface PetCallbacks<K, T : ListItem<K>> : ItemViewClickListener<T>,
        ItemViewLongClickListener<T>, AvatarClickListener<T>,
        FavouriteClickListener<T>, MoreClickListener<T>, DeleteClickListener<K, T>,
        ItemHighlighter<T>
}