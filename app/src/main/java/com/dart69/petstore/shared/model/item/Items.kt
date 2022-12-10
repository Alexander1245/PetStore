package com.dart69.petstore.shared.model.item

import java.io.Serializable

/** @param K is primary key type. Keys mustn't duplicates.
 * Useful in cases when your items require unique parameter
 * like in databases or DiffUtil item callbacks. */
interface UniqueItem<K> {
    val id: K
}

interface ListItem {
    val name: String
    val details: String
    val avatarUri: String
}

interface FavouriteItem {
    val isFavourite: Boolean

    fun makeFavourite(): FavouriteItem

    fun unmakeFavourite(): FavouriteItem
}

interface SelectableItem {
    val isSelected: Boolean

    fun select(): SelectableItem

    fun unselect(): SelectableItem
}

interface StoreItem<K> : UniqueItem<K>, FavouriteItem, ListItem, Serializable

interface SelectableStoreItem<K> : StoreItem<K>, SelectableItem

@Suppress("UNCHECKED_CAST")
fun <T : FavouriteItem> T.toggleFavourite(): T {
    val favouriteItem = if (isFavourite) unmakeFavourite() else makeFavourite()
    return favouriteItem as T
}