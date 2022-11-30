package com.dart69.petstore.shared.presentation

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dart69.petstore.shared.model.item.ListItem
import com.dart69.petstore.shared.presentation.recyclerview.ListItemDiffUtilItemCallback

abstract class SelectableAdapter<K, T : ListItem<K>, VB : ViewBinding, VH : SelectableAdapter.SelectableViewHolder<K, T, VB>>(
    diffUtilItemCallback: DiffUtil.ItemCallback<T> = ListItemDiffUtilItemCallback()
) : ListAdapter<T, SelectableAdapter.SelectableViewHolder<K, T, VB>>(diffUtilItemCallback) {

    abstract class SelectableViewHolder<K, T : ListItem<K>, VB : ViewBinding>(
        binding: VB
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }
}