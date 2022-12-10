package com.dart69.petstore.shared.model

import com.dart69.petstore.model.TestUniqueItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal fun createItems(count: Int): List<TestUniqueItem> {
    return List(count) {
        TestUniqueItem(it.toLong(), title = "Title $it", description = "Description $it")
    }
}

internal class ItemSelectionTrackerTest {
    private lateinit var tracker: ItemSelectionTracker<Long, TestUniqueItem>

    @BeforeEach
    fun beforeEach() {
        tracker = ItemSelectionTracker()
    }

    @Test
    fun observe() {
        assertEquals(emptySet<Long>(), tracker.observe().value)
        val items = createItems(10)
        tracker.select(items)
        assertEquals(items.map { it.id }.toSet(), tracker.observe().value)
    }

    @Test
    fun select() {
        val items = createItems(10)
        tracker.select(items)
        tracker.select(items)
        tracker.select(items)
        assertEquals(items.map { it.id }.toSet(), tracker.observe().value)
    }

    @Test
    fun unselect() {
        val items = createItems(10)
        tracker.select(items)
        tracker.unselect(items)
        assertEquals(emptySet<Long>(), tracker.observe().value)
    }

    @Test
    fun clear() {
        val items = createItems(10)
        tracker.select(items)
        tracker.clear()
        assertEquals(emptySet<Long>(), tracker.observe().value)
    }

    @Test
    fun isSelected() {
        val items = createItems(10)
        tracker.select(items)
        items.forEach {
            assertEquals(true, tracker.isSelected(it))
        }
    }

    @Test
    fun count() {
        val items = createItems(10)
        tracker.select(items)
        assertEquals(items.count(), tracker.count())
    }
}