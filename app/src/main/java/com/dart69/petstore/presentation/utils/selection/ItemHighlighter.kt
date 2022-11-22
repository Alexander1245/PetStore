package com.dart69.petstore.presentation.utils.selection

import android.view.View
import com.dart69.petstore.presentation.home.recyclerview.Selectable

interface ItemHighlighter<T: Selectable> {
    fun highlight(view: View, model: T)
}