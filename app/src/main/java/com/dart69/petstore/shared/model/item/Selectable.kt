package com.dart69.petstore.shared.model.item

interface Selectable {
    val isSelected: Boolean

    fun select(): Selectable

    fun unselect(): Selectable

    fun toggle(): Selectable
}