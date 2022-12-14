package com.dart69.petstore.data.repository

import com.dart69.petstore.model.TestUniqueItem
import com.dart69.petstore.presentation.TestDispatchers
import com.dart69.petstore.shared.data.repository.ItemRepository

internal class UniqueItemsRepositoryImplementationTest {
    private val dataSize = 30
    private lateinit var fakeData: List<TestUniqueItem>
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var repository: ItemRepository<Long, TestUniqueItem>
}