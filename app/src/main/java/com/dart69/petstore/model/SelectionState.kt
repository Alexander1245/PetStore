package com.dart69.petstore.model

data class SelectionState(
    val selected: Int,
    val total: Int
) {
    companion object {
        val EMPTY = SelectionState(0, 0)
    }
}
