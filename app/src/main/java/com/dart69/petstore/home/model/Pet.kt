package com.dart69.petstore.home.model

import com.dart69.petstore.shared.model.item.StoreItem

data class Pet(
    override val id: Long,
    override val name: String = "",
    override val details: String = "",
    override val isFavourite: Boolean = false,
    override val avatarUri: String = ""
) : StoreItem<Long> {
    override fun makeFavourite(): Pet = copy(isFavourite = true)

    override fun unmakeFavourite(): Pet = copy(isFavourite = false)
}
