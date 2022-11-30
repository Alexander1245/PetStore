package com.dart69.petstore.presentation.utils.selection

import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.presentation.recyclerview.PetItem
import org.junit.Assert.assertEquals
import org.junit.Test

internal class TrackerTest {
    private val tracker: Tracker<Long, PetItem>
        get() = Tracker.Implementation()

    @Test
    fun testToggleAndIsSelected() {
        val selectionTracker = tracker
        val item = PetItem(Pet(1), false)
        val notSelectedItem = PetItem(Pet(2), false)
        selectionTracker.toggle(item.id)
        assertEquals(true, selectionTracker.isSelected(item.id))
        assertEquals(false, selectionTracker.isSelected(notSelectedItem.id))
    }

    @Test
    fun selectAll() {
        val selectionTracker = tracker
        val size = 10
        val items = List(size) {
            PetItem(Pet(it.toLong()), false)
        }
        val notSelectedItems = List(size) {
            val id = ((it + size) * 2).toLong()
            PetItem(Pet(id), false)
        }
        selectionTracker.selectAll(items.map { it.id })
        items.forEach { assertEquals(true, selectionTracker.isSelected(it.id)) }
        notSelectedItems.forEach { assertEquals(false, selectionTracker.isSelected(it.id)) }
    }

    @Test
    fun unselectAll() {
        val selectionTracker = tracker
        val size = 10
        val items = List(size) {
            PetItem(Pet(it.toLong()), false)
        }
        val notSelectedItems = List(size) {
            val id = ((it + size) * 2).toLong()
            PetItem(Pet(id), false)
        }
        selectionTracker.selectAll(items.map { it.id })
        selectionTracker.unselectAll(items.map { it.id })
        items.forEach { assertEquals(false, selectionTracker.isSelected(it.id)) }
        notSelectedItems.forEach { assertEquals(false, selectionTracker.isSelected(it.id)) }
    }
}