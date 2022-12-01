package com.dart69.petstore.shared.model.item

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

interface StoreItem<K> : UniqueItem<K>, FavouriteItem, ListItem

interface SelectableStoreItem<K> : StoreItem<K>, SelectableItem

@Suppress("UNCHECKED_CAST")
data class SelectableWrapper<K, T : StoreItem<K>>(
    val source: T,
    override val isSelected: Boolean
) : SelectableStoreItem<K>, StoreItem<K> by source {
    override fun select(): SelectableItem = copy(isSelected = true)

    override fun unselect(): SelectableItem = copy(isSelected = false)

    override fun makeFavourite(): FavouriteItem = copy(source = source.makeFavourite() as T)

    override fun unmakeFavourite(): FavouriteItem = copy(source = source.unmakeFavourite() as T)
}

@Suppress("UNCHECKED_CAST")
fun <T : SelectableItem> T.toggleSelected(): T {
    val selectableItem = if (isSelected) unselect() else select()
    return selectableItem as T
}

@Suppress("UNCHECKED_CAST")
fun <T : FavouriteItem> T.toggleFavourite(): T {
    val favouriteItem = if (isFavourite) unmakeFavourite() else makeFavourite()
    return favouriteItem as T
}