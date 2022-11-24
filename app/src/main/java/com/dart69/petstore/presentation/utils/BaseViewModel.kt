package com.dart69.petstore.presentation.utils

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** Extensions below are designed to remove duplicate Mutable live data and live data and
 * provide easy data mapping functional.
 * How to use: Just extend your ViewModel class from BaseViewModel. */
open class BaseViewModel : ViewModel() {

    /** Instantiate LiveData with initial value and notify all subscribers */
    protected fun <T> liveDataOf(initial: T): LiveData<T> =
        MutableLiveData<T>().apply { updateValue(initial) }

    protected fun <T> liveEventOf(initial: T): LiveEvent<T> =
        MutableLiveEvent<T>().apply { updateEventValue(initial) }

    protected fun <T> LiveData<T>.value(): T = value ?: error("Value not set.")

    protected fun <T> LiveData<T>.updateValue(value: T) {
        this as MutableLiveData
        val update = if(Looper.myLooper() == Looper.getMainLooper()) this::setValue else this::postValue
        update(value)
    }

    protected fun <T> LiveData<Event<T>>.updateEventValue(value: T) {
        updateValue(Event(value))
    }

    protected fun <T> LiveData<T>.modifyValue(transform: (T) -> T) {
        updateValue(transform(value ?: return))
    }

    /**@param transform: applied when source LiveData changed,
     * it take two arguments: new value from source LiveData and current value from result. */
    fun <T, R> LiveData<T>.map(initial: R, transform: (T, R) -> R): LiveData<R> {
        val result = MediatorLiveData<R>().apply { updateValue(initial) }
        result.addSource(this) {
            result.updateValue(transform(it, result.value!!))
        }
        return result
    }
}