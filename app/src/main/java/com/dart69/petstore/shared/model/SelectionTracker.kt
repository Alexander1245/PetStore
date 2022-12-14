package com.dart69.petstore.shared.model

import com.dart69.petstore.shared.model.item.UniqueItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectionTracker<K, T : UniqueItem<K>> {
    fun observe(): StateFlow<Set<K>>

    fun select(items: List<T>)

    fun unselect(items: List<T>)

    fun clear()

    fun isSelected(item: T): Boolean

    fun count(): Int
}

class ItemSelectionTracker<K, T : UniqueItem<K>>(
    logger: Logger = Logger.None
) : SelectionTracker<K, T> {
    private val keys = MutableStateFlow(emptySet<K>())

    init {
        logger.logEachEmit(javaClass.name, emptySet(), keys)
    }

    override fun observe(): StateFlow<Set<K>> = keys.asStateFlow()

    override fun select(items: List<T>) {
        val selectSingle = lambda@{ item: T ->
            if (item.id in keys.value) return@lambda
            keys.update { keys -> keys + item.id }
        }
        items.forEach(selectSingle)
    }

    override fun unselect(items: List<T>) {
        val unselectSingle = lambda@{ item: T ->
            if (item.id !in keys.value) return@lambda
            keys.update { keys -> keys - item.id }
        }
        items.forEach(unselectSingle)
    }

    override fun clear() = keys.update { emptySet() }

    override fun isSelected(item: T): Boolean = item.id in keys.value

    override fun count(): Int = keys.value.size
}

fun <K, T : UniqueItem<K>> SelectionTracker<K, T>.toggle(item: T) {
    if (isSelected(item)) unselect(item) else select(item)
}

fun <K, T : UniqueItem<K>> SelectionTracker<K, T>.select(item: T) {
    select(listOf(item))
}

fun <K, T : UniqueItem<K>> SelectionTracker<K, T>.unselect(item: T) {
    unselect(listOf(item))
}
