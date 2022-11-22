package com.dart69.petstore.presentation.home.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.dart69.petstore.model.item.ListItem

class ListItemDiffUtilItemCallback<K, T : ListItem<K>>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}