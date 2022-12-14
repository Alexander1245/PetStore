package com.dart69.petstore.presentation.utils.selection

import com.dart69.petstore.home.domain.model.Pet
import com.dart69.petstore.home.domain.model.SelectablePet
import com.dart69.petstore.home.domain.model.makeSelectable
import com.dart69.petstore.shared.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

internal class TrackerTest {
    private val tracker: SelectionTracker<Long, SelectablePet>
        get() = ItemSelectionTracker()

    @Test
    fun testToggleAndIsSelected() {
        val selectionTracker = tracker
        val item = Pet(1).makeSelectable(false)
        val notSelectedItem = Pet(2).makeSelectable(false)
        selectionTracker.toggle(item)
        assertEquals(true, selectionTracker.isSelected(item))
        assertEquals(false, selectionTracker.isSelected(notSelectedItem))
    }

    @Test
    fun selectAll() {
        val selectionTracker = tracker
        val size = 10
        val items = List(size) {
            Pet(it.toLong()).makeSelectable(false)
        }
        val notSelectedItems = List(size) {
            val id = ((it + size) * 2).toLong()
            Pet(id).makeSelectable(false)
        }
        selectionTracker.select(items)
        items.forEach { assertEquals(true, selectionTracker.isSelected(it)) }
        notSelectedItems.forEach { assertEquals(false, selectionTracker.isSelected(it)) }
    }

    @Test
    fun unselectAll() {
        val selectionTracker = tracker
        val size = 10
        val items = List(size) {
            Pet(it.toLong()).makeSelectable(false)
        }
        val notSelectedItems = List(size) {
            val id = ((it + size) * 2).toLong()
            Pet(id).makeSelectable(false)
        }
        selectionTracker.select(items)
        selectionTracker.unselect(items)
        items.forEach { assertEquals(false, selectionTracker.isSelected(it)) }
        notSelectedItems.forEach { assertEquals(false, selectionTracker.isSelected(it)) }
    }
}