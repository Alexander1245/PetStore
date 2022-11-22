package com.dart69.petstore.presentation.utils.selection

import android.util.Log
import com.dart69.petstore.model.extensions.clearIfNotEmpty
import com.dart69.petstore.model.item.SelectableItem

interface Tracker<K, T: SelectableItem<K>> {
    val keys: Set<K>

    fun count(): Int

    fun hasSelection(): Boolean

    fun isSelected(key: K): Boolean

    fun toggle(key: K)

    fun select(key: K)

    fun unselect(key: K)

    fun selectAll(keys: List<K>)

    fun unselectAll(keys: List<K>)

    fun clear()

    fun addOnChangedListener(listener: () -> Unit)

    class Implementation<K, T: SelectableItem<K>>: Tracker<K, T> {
        private val itemKeys = HashSet<K>()
        private var listener: (() -> Unit)? = null

        override val keys: Set<K> = itemKeys

        override fun count(): Int = itemKeys.size

        override fun hasSelection(): Boolean = itemKeys.isNotEmpty()

        override fun isSelected(key: K): Boolean = key in itemKeys

        private fun invokeListener(keys: List<K>, action: (List<K>) -> Boolean) {
            val hasBeenChanged = action(keys)
            if(hasBeenChanged) {
                listener?.invoke()
                Log.d("selection", "listener invoked. Selected items ids: ${this.itemKeys}")
            }
        }

        override fun toggle(key: K) {
            val action = if(isSelected(key)) this::unselectAll else this::selectAll
            action(listOf(key))
        }

        override fun select(key: K) {
            selectAll(listOf(key))
        }

        override fun unselect(key: K) {
            unselectAll(listOf(key))
        }

        override fun selectAll(keys: List<K>) {
            invokeListener(keys, itemKeys::addAll)
        }

        override fun unselectAll(keys: List<K>) {
            invokeListener(keys, itemKeys::removeAll)
        }

        override fun clear() {
            if(itemKeys.clearIfNotEmpty()) {
                listener?.invoke()
                Log.d("selection", "listener invoked. Selected items ids: ${this.itemKeys}")
            }
        }

        override fun addOnChangedListener(listener: () -> Unit) {
            this.listener = listener
        }
    }
}