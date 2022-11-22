package com.dart69.petstore.model

sealed class Progress {
    class Error(val exception: Exception): Progress()

    object Completed : Progress()

    object Active : Progress()
}
