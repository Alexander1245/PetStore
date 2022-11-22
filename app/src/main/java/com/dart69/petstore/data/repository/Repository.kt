package com.dart69.petstore.data.repository

import com.dart69.petstore.model.item.Item

interface Repository {
    interface ReadOnly<K, T : Item.Unique<K>> : Repository {
        suspend fun getAll(): List<T>

        suspend fun findByPrimaryKey(key: K): T
    }

    interface WriteOnly<K, T : Item.Unique<K>> : Repository {
        /** @return key of item that has been inserted */
        suspend fun insert(item: T): K

        /** @return list of keys that have been inserted */
        suspend fun insertMany(items: List<T>): List<K>
    }

    interface EditOnly<K, T : Item.Unique<K>> : Repository {
        /** @return count of updated items */
        suspend fun update(item: T): Int

        /** @return count of updated items */
        suspend fun updateMany(items: List<T>): Int
    }

    interface DeleteOnly<K, T : Item.Unique<K>> : Repository {
        /** @return count of deleted items */
        suspend fun delete(item: T): Int

        /** @return count of deleted items */
        suspend fun deleteMany(items: List<T>): Int
    }

    interface Mutable<K, T : Item.Unique<K>> : ReadOnly<K, T>, WriteOnly<K, T>, EditOnly<K, T>,
        DeleteOnly<K, T>
}