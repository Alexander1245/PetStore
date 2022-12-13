package com.dart69.petstore.shared.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.item.UniqueItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

interface DataSource<K, T : UniqueItem<K>> {
    suspend fun fetch(): List<T>

    suspend fun delete(items: List<T>)

    suspend fun update(items: List<T>)
}

class FakeLocalDataSource<K, T : UniqueItem<K>>(
    initialData: List<T>,
    private val dispatchers: AvailableDispatchers,
) : DataSource<K, T> {
    private val data = MutableStateFlow(initialData)

    override suspend fun fetch(): List<T> = data.value

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun delete(items: List<T>) = withContext(dispatchers.io) {
        val keys = items.map { it.id }.toSet()
        data.update { oldList ->
            oldList.toMutableList().apply {
                removeIf { item -> item.id in keys }
            }
        }
    }

    override suspend fun update(items: List<T>) = withContext(dispatchers.io) {
        val updateSingle: MutableList<T>.(T) -> Unit = lambda@{ item: T ->
            val index = indexOfFirst { it.id == item.id }
            this[if (index != -1) index else return@lambda] = item
        }
        val uniqueItems = items.toSet()
        data.update { oldList ->
            oldList.toMutableList().apply {
                uniqueItems.forEach { updateSingle(it) }
            }
        }
    }
}