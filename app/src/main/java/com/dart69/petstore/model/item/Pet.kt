package com.dart69.petstore.model.item

data class Pet(
    override val id: Long,
    override val name: String = "",
    override val details: String = "",
    override val isFavourite: Boolean = false,
    override val avatarUri: String = ""
) : Item.HasAll<Long> {
    override fun makeFavourite(): Pet = copy(isFavourite = true)

    override fun unmakeFavourite(): Pet = copy(isFavourite = false)

    override fun toggleFavourite(): Item.CanBeFavourite =
        if (isFavourite) unmakeFavourite() else makeFavourite()
}
