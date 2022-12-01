package com.dart69.petstore.shared.data.repository

import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.item.UniqueItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class FakeRepository<K, T : UniqueItem<K>>(
    initialData: List<T>,
    private val dispatchers: AvailableDispatchers
) : ItemRepository<K, T> {
    private val data = initialData.toMutableList()
    private val mutableTask = MutableStateFlow(Task.initial(emptyList<T>()))

    private suspend fun <T> MutableStateFlow<Task<T>>.newTask(resultBlock: suspend () -> T) {
        try {
            value = Task.Loading()
            value = Task.Completed(resultBlock())
        } catch (exception: Exception) {
            value = Task.Error(exception.message)
        }
    }

    override suspend fun initialize() = withContext(dispatchers.default) {
        mutableTask.newTask(::getAll)
    }

    override fun observe(): StateFlow<Task<List<T>>> = mutableTask.asStateFlow()

    override suspend fun getAll(): List<T> = withContext(dispatchers.default) {
        delay(1500L)
        data
    }

    override suspend fun update(items: List<T>) {
        val updateSingle: MutableList<T>.(T) -> Unit = lambda@{ item: T ->
            val index = indexOfFirst { it.id == item.id }
            this[if (index != -1) index else return@lambda] = item
        }
        mutableTask.newTask {
            delay(500L)
            val uniqueItems = items.toSet()
            data.apply {
                uniqueItems.forEach { updateSingle(it) }
            }
        }
    }

    override suspend fun delete(items: List<T>) {
        mutableTask.newTask {
            delay(500)
            data -= items.toSet()
            data
        }
    }
}