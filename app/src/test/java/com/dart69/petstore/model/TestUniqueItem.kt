package com.dart69.petstore.model

import com.dart69.petstore.shared.model.item.Item

data class TestUniqueItem(
    override val id: Long,
    val title: String
) : Item.Unique<Long>