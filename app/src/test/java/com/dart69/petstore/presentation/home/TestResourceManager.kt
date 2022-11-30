package com.dart69.petstore.presentation.home

import com.dart69.petstore.shared.presentation.ResourceManager

class TestResourceManager : ResourceManager {
    override fun getString(stringRes: Int, vararg formatArgs: Any): String {
        return "$stringRes"
    }
}