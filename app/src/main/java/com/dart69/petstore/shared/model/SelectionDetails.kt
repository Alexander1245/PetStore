package com.dart69.petstore.shared.model

data class SelectionDetails(
    val selected: Int,
    val total: Int
) {
    init {
        assert(selected >= 0 && total >= 0)
    }

    companion object {
        val EMPTY = SelectionDetails(0, 0)
    }
}

fun SelectionDetails.hasSelected(): Boolean = selected > 0

fun SelectionDetails.allSelected(): Boolean = selected == total