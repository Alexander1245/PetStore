package com.dart69.petstore.shared.presentation

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dart69.petstore.shared.model.item.UniqueItem
import com.dart69.petstore.shared.presentation.recyclerview.UniversalItemCallback

abstract class ItemAdapter<K, T : UniqueItem<K>, VB : ViewBinding, VH : ItemViewHolder<K, T, VB>>(
    diffUtilItemCallback: DiffUtil.ItemCallback<T> = UniversalItemCallback()
) : ListAdapter<T, ItemViewHolder<K, T, VB>>(diffUtilItemCallback)

abstract class ItemViewHolder<K, T : UniqueItem<K>, VB : ViewBinding>(
    binding: VB
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: T)
}