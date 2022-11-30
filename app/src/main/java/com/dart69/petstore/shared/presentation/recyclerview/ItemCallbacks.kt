package com.dart69.petstore.shared.presentation.recyclerview

interface ItemCallbacks {
    fun interface ItemViewClickListener<T> {
        fun onItemViewClick(item: T)
    }

    fun interface ItemViewLongClickListener<T> {
        fun onItemViewLongClick(item: T): Boolean
    }
}