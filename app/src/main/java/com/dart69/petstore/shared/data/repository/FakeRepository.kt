package com.dart69.petstore.shared.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dart69.petstore.shared.data.DataSource
import com.dart69.petstore.shared.emitTasks
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.item.UniqueItem
import com.dart69.petstore.shared.model.takeResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class FakeRepository<K, T : UniqueItem<K>>(
    private val localDataSource: DataSource<K, T>,
    private val dispatchers: AvailableDispatchers,
    private val delayTime: Long = 1500L
) : ItemRepository<K, T> {
    private val task = MutableStateFlow(Task.initial(emptyList<T>()))

    override suspend fun refresh() = withContext(dispatchers.default) {
        task.emitTasks(::retrieve)
    }

    override fun observe(): StateFlow<Task<List<T>>> = task.asStateFlow()

    override suspend fun retrieve(): List<T> {
        val currentList = task.value.takeResult()
        return if (currentList.isNullOrEmpty()) {
            delay(delayTime)
            localDataSource.fetch()
        } else currentList
    }

    override suspend fun update(items: List<T>) = withContext(dispatchers.default) {
        task.emitTasks {
            delay(delayTime)
            localDataSource.update(items)
            localDataSource.fetch()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun delete(items: List<T>) = withContext(dispatchers.default) {
        task.emitTasks {
            delay(delayTime)
            localDataSource.delete(items)
            localDataSource.fetch()
        }
    }
}