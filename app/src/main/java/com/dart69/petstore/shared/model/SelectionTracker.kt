package com.dart69.petstore.shared.model

import com.dart69.petstore.shared.model.item.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectionTracker<K, T : Item.Unique<K>> {
    fun observe(): StateFlow<Set<K>>

    fun select(vararg items: T)

    fun unselect(vararg items: T)

    fun clear()

    fun isSelected(item: T): Boolean

    fun count(): Int
}

inline fun <K, reified T : Item.Unique<K>> SelectionTracker<K, T>.toggle(item: T) {
    val action = if (isSelected(item)) this::unselect else this::select
    action(arrayOf(item))
}

inline fun <K, reified T : Item.Unique<K>> SelectionTracker<K, T>.selectMany(list: List<T>) {
    select(*list.toTypedArray())
}

inline fun <K, reified T : Item.Unique<K>> SelectionTracker<K, T>.unselectMany(list: List<T>) {
    unselect(*list.toTypedArray())
}

class ItemSelectionTracker<K, T : Item.Unique<K>>(
    logger: Logger = Logger.None
) : SelectionTracker<K, T> {
    private val keys = MutableStateFlow(emptySet<K>())

    init {
        logger.logEachEmit(javaClass.name, emptySet(), keys)
    }

    override fun observe(): StateFlow<Set<K>> = keys.asStateFlow()

    override fun select(vararg items: T) {
        val selectSingle = lambda@{ item: T ->
            if (item.id in keys.value) return@lambda
            keys.update { keys -> keys + item.id }
        }
        items.forEach(selectSingle)
    }

    override fun unselect(vararg items: T) {
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

