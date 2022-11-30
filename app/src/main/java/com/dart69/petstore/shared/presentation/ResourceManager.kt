package com.dart69.petstore.shared.presentation

import android.content.Context
import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringRes: Int, vararg formatArgs: Any): String

    class Implementation(
        context: Context
    ): ResourceManager {
        private val applicationContext = context.applicationContext

        override fun getString(stringRes: Int, vararg formatArgs: Any): String =
            applicationContext.getString(stringRes, formatArgs)
    }
}