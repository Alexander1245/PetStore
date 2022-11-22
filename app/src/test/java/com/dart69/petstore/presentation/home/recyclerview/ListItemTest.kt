package com.dart69.petstore.presentation.home.recyclerview

import com.dart69.petstore.model.item.ListItem
import com.dart69.petstore.model.item.Pet
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ListItemTest {

    @Test
    fun testEquals() {
        val source = Pet(1)
        val item = ListItem.Implementation(source, false)
        val selected = item.select()
        assertEquals(false, selected == item)
        assertEquals(false, selected.isSelected == item.isSelected)
    }

    @Test
    fun testGetId() {
        val source1 = Pet(1)
        val item1 = ListItem.Implementation(source1, false)
        val source2 = Pet(1337)
        val item2 = ListItem.Implementation(source2, true)
        assertEquals(source1.id, item1.id)
        assertEquals(source2.id, item2.id)
    }
}