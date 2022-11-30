package com.dart69.petstore.shared.model.item

interface Item {
    /**@param K is key type to identify that item is unique
     * */
    interface Unique<K>: Item {
        val id: K

        override fun equals(other: Any?): Boolean

        override fun hashCode(): Int
    }

    interface CanBeFavourite {
        val isFavourite: Boolean

        fun makeFavourite(): CanBeFavourite

        fun unmakeFavourite(): CanBeFavourite

        fun toggleFavourite(): CanBeFavourite
    }

    interface HasAvatar {
        val avatarUri: String
    }

    interface HasDetails {
        val details: String
    }

    interface HasName {
        val name: String
    }

    interface HasAll<K>: HasName, HasDetails, HasAvatar, CanBeFavourite, Unique<K>
}