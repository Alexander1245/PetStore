package com.dart69.petstore.shared.presentation.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.dart69.petstore.shared.model.item.ListItem

class ListItemDiffUtilItemCallback<K, T : ListItem<K>>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}