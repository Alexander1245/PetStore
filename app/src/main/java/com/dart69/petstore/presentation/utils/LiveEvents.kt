package com.dart69.petstore.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

var <T> MutableLiveEvent<T>.eventValue: T?
    get() = value?.getContentIfNotHandled()
    set(newValue) { newValue?.let { content -> value = Event(content) } }

fun <T> MutableLiveEvent<T>.postEvent(content: T) {
    postValue(Event(content))
}