package com.dart69.petstore.presentation.utils.selection

import com.dart69.petstore.model.TestUniqueItem
import com.dart69.petstore.shared.model.ItemSelectionTracker
import kotlinx.coroutines.flow.StateFlow
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewSelectionTrackerImplementationTest {
    private lateinit var selectionTracker: ItemSelectionTracker<Long, TestUniqueItem>
    private lateinit var items: StateFlow<List<TestUniqueItem>>

    @BeforeEach
    fun before() {
        selectionTracker = ItemSelectionTracker()
        items = selectionTracker.observe
    }

    @Test
    fun testGetSelectedItems() {
        val expected = List(10) {
            TestUniqueItem(it.toLong(), "${-it}")
        }.toTypedArray()
        selectionTracker.select(*expected)
        assertArrayEquals(expected, items.value.toTypedArray())
    }

    @Test
    fun testSelectSingleItem() {
        val expected = List(10) {
            TestUniqueItem(it.toLong(), "Title $it")
        }.toTypedArray().onEach { selectionTracker.select(it) }
        assertArrayEquals(expected, items.value.toTypedArray())
    }

    @Test
    fun testSelectSameItemMultipleTimes() {
        val item = listOf(TestUniqueItem(11, "Eleven")).toTypedArray()
        repeat(10) {
            selectionTracker.select(*item)
        }
        assertArrayEquals(item, items.value.toTypedArray())
    }

    @Test
    fun testSelectManyItems() {
        val expected = List(10) {
            TestUniqueItem(it.times(it).toLong(), "Title $it")
        }.toTypedArray()
        selectionTracker.select(*expected)
        assertArrayEquals(expected, items.value.toTypedArray())
    }

    @Test
    fun testUnselectSingleItem() {
        val item = listOf(TestUniqueItem(11, "Eleven")).toTypedArray()
        selectionTracker.select(*item)
        repeat(10) {
            selectionTracker.unselect(*item)
        }
        assertArrayEquals(emptyArray(), items.value.toTypedArray())
    }

    @Test
    fun testUnselectMultipleItems() {
        val list = List(10) {
            TestUniqueItem(it.toLong(), "111$it")
        }.toTypedArray()
        selectionTracker.select(*list)
        repeat(10) {
            selectionTracker.unselect(*list)
        }
        assertArrayEquals(emptyArray(), items.value.toTypedArray())
    }

    @Test
    fun clear() {
        val list = List(10) {
            TestUniqueItem(it.toLong(), "111$it")
        }.toTypedArray()
        selectionTracker.select(*list)
        selectionTracker.clear()
        assertArrayEquals(emptyArray(), items.value.toTypedArray())
    }

    @Test
    fun testIsSingleItemSelected() {
        val item = listOf(TestUniqueItem(1, "a")).toTypedArray()
        selectionTracker.select(*item)
        assertTrue(selectionTracker.isSelected(item.first()))
    }
}