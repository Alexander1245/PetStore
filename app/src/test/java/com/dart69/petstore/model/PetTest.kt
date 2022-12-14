package com.dart69.petstore.model

import com.dart69.petstore.home.model.Pet
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PetTest {

    @Test
    fun getId() {
        val pet1 = Pet(1)
        assertEquals(1, pet1.id)
        val pet2 = Pet(1337)
        assertEquals(1337, pet2.id)
    }

    @Test
    fun testEquals() {
        val pet1 = Pet(1)
        val pet2 = Pet(2)
        val pet3 = pet2.copy()
        assertEquals(false, pet1 == pet2)
        assertEquals(true, pet2 == pet3)
        assertEquals(false, pet2 === pet3)
    }
}