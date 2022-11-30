package com.dart69.petstore.data.repository

import com.dart69.petstore.model.TestUniqueItem
import com.dart69.petstore.presentation.TestDispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UniqueItemsRepositoryImplementationTest {
    private val dataSize = 30
    private lateinit var fakeData: List<TestUniqueItem>
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var repository: UniqueItemsRepository<Long, TestUniqueItem>

    @BeforeEach
    fun before() {
        fakeData = List(dataSize) { TestUniqueItem(it.toLong(), "Title $it") }
        testDispatchers = TestDispatchers()
        repository = UniqueItemsRepositoryImplementation(fakeData, testDispatchers)
    }

    @Test
    fun getItems() = runBlocking {
        val expected = fakeData.toTypedArray()
        val actual = repository.items.value.toTypedArray()
        Assertions.assertArrayEquals(expected, actual)
    }

    @Test
    fun deleteSingleItem() = runBlocking {
        repeat(dataSize / 2) {
            val item = fakeData[it]
            repository.delete(item)
            fakeData = fakeData - item
            val expected = fakeData.toTypedArray()
            val actual = repository.items.value.toTypedArray()
            Assertions.assertArrayEquals(actual, expected)
        }
        val actual = fakeData.toTypedArray()
        val expected = repository.items.value.toTypedArray()
        Assertions.assertArrayEquals(actual, expected)
    }

    @Test
    fun deleteManyItems() = runBlocking {
        val removable = fakeData.slice(5..10).toTypedArray()
        val expected = fakeData - removable.toSet()
        repository.delete(*removable)
        assertEquals(expected, repository.items.value)
    }

    @Test
    fun updateSingleItem() = runBlocking {
        val newTitle = "New title at index 0."
        val item = TestUniqueItem(0, newTitle)
        repository.update(item)
        assertEquals(item, repository.items.value.first())
    }

    @Test
    fun updateManyItems() = runBlocking {
        repository.update(*(5..10).map {
            TestUniqueItem(it.toLong(), "New title $it")
        }.toTypedArray())
        val expected = fakeData.toMutableList().apply {
            (5..10).forEach { index ->
                val item = this[index]
                this[index] = item.copy(title = "New title $index")
            }
        }.toTypedArray()
        val actual = repository.items.value.toTypedArray()
        Assertions.assertArrayEquals(expected, actual)
    }
}