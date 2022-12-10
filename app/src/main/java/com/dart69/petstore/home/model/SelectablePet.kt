package com.dart69.petstore.home.model

import com.dart69.petstore.shared.model.item.SelectableItem
import com.dart69.petstore.shared.model.item.SelectableStoreItem
import com.dart69.petstore.shared.model.item.StoreItem

interface SelectablePet : SelectableStoreItem<Long>

@Suppress("UNCHECKED_CAST")
private data class Wrapper<K, T : StoreItem<K>>(
    private val source: T,
    override val isSelected: Boolean
) : SelectableStoreItem<K>, StoreItem<K> by source {
    override fun select(): Wrapper<K, T> = copy(isSelected = true)

    override fun unselect(): Wrapper<K, T> = copy(isSelected = false)

    override fun makeFavourite(): Wrapper<K, T> = copy(source = source.makeFavourite() as T)

    override fun unmakeFavourite(): Wrapper<K, T> = copy(source = source.unmakeFavourite() as T)
}

@Suppress("UNCHECKED_CAST")
private data class PetWrapper(
    private val source: SelectableStoreItem<Long>
) : SelectablePet, SelectableStoreItem<Long> by source {
    override fun select(): PetWrapper = copy(source = source.select() as SelectableStoreItem<Long>)

    override fun unselect(): PetWrapper =
        copy(source = source.unselect() as SelectableStoreItem<Long>)

    override fun makeFavourite(): PetWrapper =
        copy(source = source.makeFavourite() as SelectableStoreItem<Long>)

    override fun unmakeFavourite(): PetWrapper =
        copy(source = source.unmakeFavourite() as SelectableStoreItem<Long>)
}

fun <K> StoreItem<K>.makeSelectable(isSelected: Boolean = false): SelectableStoreItem<K> =
    Wrapper(this, isSelected)

fun Pet.makeSelectable(isSelected: Boolean = false): SelectablePet =
    PetWrapper((this as StoreItem<Long>).makeSelectable(isSelected))

@Suppress("UNCHECKED_CAST")
fun <T : SelectableItem> T.selectIf(predicate: (T) -> Boolean): T {
    val result = if (predicate(this)) select() else unselect()
    return result as T
}