package com.dart69.petstore.model

import com.dart69.petstore.shared.model.item.UniqueItem

data class TestUniqueItem(
    override val id: Long,
    val title: String,
    val description: String = title + title
) : UniqueItem<Long>