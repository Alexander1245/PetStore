package com.dart69.petstore.model.item

import com.dart69.petstore.presentation.home.recyclerview.Selectable

interface ListItem<K> : Item.HasAll<K>, SelectableItem<K> {
    fun <T: Item.HasAll<K>> map(): T

    data class Implementation<K, T : Item.HasAll<K>>(
        private val source: T,
        override val isSelected: Boolean
    ) : ListItem<K> {
        override val id: K = source.id

        override val name: String = source.name

        override val details: String = source.details

        override val isFavourite: Boolean = source.isFavourite

        override val avatarUri: String = source.avatarUri

        override fun select(): Selectable = copy(isSelected = true)

        override fun unselect(): Selectable = copy(isSelected = false)

        override fun toggle(): Selectable = if (isSelected) unselect() else select()

        override fun makeFavourite(): Implementation<K, T> =
            copy(source = source.makeFavourite() as T)

        override fun unmakeFavourite(): Implementation<K, T> =
            copy(source = source.unmakeFavourite() as T)

        override fun toggleFavourite(): Item.CanBeFavourite =
            copy(source = source.toggleFavourite() as T)

        override fun <T : Item.HasAll<K>> map(): T = source as T
    }
}