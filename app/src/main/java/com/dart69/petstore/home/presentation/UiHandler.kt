package com.dart69.petstore.home.presentation

import androidx.annotation.IdRes

interface UiHandler {
    fun onGroupActionItemClick(@IdRes id: Int): Boolean

    fun onActionButtonClick()
}