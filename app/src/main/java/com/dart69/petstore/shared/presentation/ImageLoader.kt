package com.dart69.petstore.shared.presentation

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

interface ImageLoader {
    fun loadInto(imageUri: String, into: ImageView)

    class GlideImplementation(
        context: Context
    ): ImageLoader {
        private val applicationContext = context.applicationContext

        override fun loadInto(imageUri: String, into: ImageView) {
            Glide.with(applicationContext)
                .load(imageUri)
                .centerCrop()
                .into(into)
        }
    }
}