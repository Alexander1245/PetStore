package com.dart69.petstore.shared.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dart69.petstore.shared.emitTasks
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.item.UniqueItem
import com.dart69.petstore.shared.model.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class FakeRepository<K, T : UniqueItem<K>>(
    initialData: List<T>,
    private val dispatchers: AvailableDispatchers,
    private val delayTime: Long = 1500L
) : ItemRepository<K, T> {
    private val taskHolder = MutableStateFlow(Task.initial(emptyList<T>()))
    private val dataHolder = MutableStateFlow(initialData)

    override suspend fun refresh() = withContext(dispatchers.default) {
        taskHolder.emitTasks(::getAll)
    }

    override fun observe(): StateFlow<Task<List<T>>> = taskHolder.asStateFlow()

    override fun findByPrimaryKey(key: K): Flow<Task<T>> = taskHolder.map { task ->
        task.map {
            dataHolder.value.find { it.id == key }
                ?: error("Can't find actual item in repository")
        }
    }

    override suspend fun getAll(): List<T> = withContext(dispatchers.default) {
        delay(delayTime)
        dataHolder.value
    }

    override suspend fun update(items: List<T>) = withContext(dispatchers.default) {
        val updateSingle: MutableList<T>.(T) -> Unit = lambda@{ item: T ->
            val index = indexOfFirst { it.id == item.id }
            this[if (index != -1) index else return@lambda] = item
        }
        taskHolder.emitTasks {
            delay(delayTime)
            val uniqueItems = items.toSet()
            dataHolder.updateAndGet { oldList ->
                oldList.toMutableList().apply {
                    uniqueItems.forEach { updateSingle(it) }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun delete(items: List<T>) = withContext(dispatchers.default) {
        taskHolder.emitTasks {
            delay(delayTime)
            val keys = items.map { it.id }.toSet()
            dataHolder.updateAndGet { oldList ->
                oldList.toMutableList().apply {
                    removeIf { item ->
                        item.id in keys
                    }
                }
            }
        }
    }
}