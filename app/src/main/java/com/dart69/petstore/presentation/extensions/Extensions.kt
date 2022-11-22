package com.dart69.petstore.presentation.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.presentation.utils.ViewModelFactory
import com.dart69.petstore.presentation.app.App

fun Context.provideFactory(): ViewModelProvider.Factory {
    val app = applicationContext as App
    return ViewModelFactory(app.provideRepository(), app.provideSelectionTracker())
}

fun Fragment.provideFactory(): ViewModelProvider.Factory =
    requireContext().provideFactory()

fun <T> MutableLiveData<T>.updateLastValue(updater: (T) -> T) {
    value = updater(value ?: return)
}

fun <T> MutableLiveData<T>.postLastValue(updater: (T) -> T) {
    postValue(value ?: return)
}

fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> =
    Transformations.map(this, mapper)

fun <T, R> LiveData<T>.switchMap(mapper: (T) -> LiveData<R>): LiveData<R> =
    Transformations.switchMap(this, mapper)

fun <T> LiveData<T>.withInitial(initial: T): LiveData<T> =
    (this as MutableLiveData<T>).apply { value = initial }
