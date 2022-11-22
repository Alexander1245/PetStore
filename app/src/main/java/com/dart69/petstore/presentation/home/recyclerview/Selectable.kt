package com.dart69.petstore.presentation.home.recyclerview

interface Selectable {
    val isSelected: Boolean

    fun select(): Selectable

    fun unselect(): Selectable

    fun toggle(): Selectable
}