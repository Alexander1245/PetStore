package com.dart69.petstore.data.repository

import com.dart69.petstore.model.item.Item

interface FavouriteItemRepository<K, T> :
    Repository.Mutable<K, T> where T : Item.Unique<K>, T : Item.CanBeFavourite {
        suspend fun getAllSortedByFavourite(): List<T>

        suspend fun deleteByKeys(keys: List<K>): Int

        suspend fun findByKeys(keys: List<K>): List<T>

        fun addOnChangedListener(listener: (List<T>) -> Unit)

        fun removeOnChangedListener()
}