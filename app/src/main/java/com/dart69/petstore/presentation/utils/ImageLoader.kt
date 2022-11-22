package com.dart69.petstore.presentation.utils

import android.widget.ImageView

interface ImageLoader {
    fun loadInto(imageUri: String, into: ImageView)
}