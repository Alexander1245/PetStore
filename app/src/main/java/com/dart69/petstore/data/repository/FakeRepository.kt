package com.dart69.petstore.data.repository

import android.net.Uri
import com.dart69.petstore.model.item.Pet
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

private val imageSources = listOf(
    "https://images.unsplash.com/photo-1615796153287-98eacf0abb13?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1579168765467-3b235f938439?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1611915387288-fd8d2f5f928b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1506755855567-92ff770e8d00?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Nnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1571988840298-3b5301d5109b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8NXx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1585699777545-355976789272?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTB8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1586289883499-f11d28aaf52f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8OXx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1577349516274-37ff88a53627?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTN8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1601373879104-b4290a56b691?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTF8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "https://images.unsplash.com/photo-1604675223954-b1aabd668078?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTh8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
)

class FakeRepository(
    dataCount: Int,
    private val dispatcher: CoroutineDispatcher
) : FavouriteItemRepository<Long, Pet> {
    private val isFirstLoad = AtomicBoolean(true)
    private var listener: ((List<Pet>) -> Unit)? = null
    private var data: MutableList<Pet> = MutableList(dataCount) {
        Pet(
            it.toLong(), Faker().cat().name(), Faker().cat().breed(), false,
            Uri.parse(imageSources[it % imageSources.size]).toString()
        )
        //TEST ONLY
    }

    override suspend fun insert(item: Pet): Long = withContext(dispatcher) {
        delay(500)
        data = (data + item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        1
    }

    override suspend fun getAllSortedByFavourite(): List<Pet> = withContext(dispatcher) {
        if (isFirstLoad.compareAndSet(true, false)) {
            delay(1500)
        }
        /*error("Internal error occurred")*/
        val result = data.sortedByDescending { it.isFavourite }
        result
    }
    
    override suspend fun getAll(): List<Pet> = withContext(dispatcher) {
        delay(1500)
        data
    }

    override suspend fun findByPrimaryKey(key: Long): Pet {
        TODO("Not yet implemented")
    }

    override suspend fun insertMany(items: List<Pet>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun findByKeys(keys: List<Long>): List<Pet> = withContext(dispatcher) {
        delay(555)
        val unique = keys.toSet()
        data.filter { it.id in unique }
    }

    override suspend fun update(item: Pet): Int = withContext(dispatcher) {
        delay(111)
        val index = data.indexOfFirst { pet -> pet.id == item.id }
        if (index == -1) {
            error("Can't update non existing item")
        }
        data = data.toMutableList().apply { this[index] = item }
        listener?.invoke(getAllSortedByFavourite())
        1
    }

    override suspend fun updateMany(items: List<Pet>): Int {
        delay(555)
        val unique = items.toSet()
        unique.forEach { update(it) }
        return unique.size
    }

    override suspend fun delete(item: Pet): Int = withContext(dispatcher) {
        delay(111)
        data = (data - item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        1
    }

    override suspend fun deleteMany(items: List<Pet>): Int = withContext(dispatcher) {
        delay(666)
        val oldSize = data.size
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        oldSize - data.size
    }

    override suspend fun deleteByKeys(keys: List<Long>): Int = withContext(dispatcher) {
        delay(666)
        val oldSize = data.size
        val items = keys.mapNotNull { key -> data.find { it.id == key } }
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        oldSize - data.size
    }

    override fun addOnChangedListener(listener: (List<Pet>) -> Unit) {
        this.listener = listener
    }

    override fun removeOnChangedListener() {
        this.listener = null
    }
}