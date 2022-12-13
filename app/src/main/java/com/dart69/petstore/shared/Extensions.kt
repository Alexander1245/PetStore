package com.dart69.petstore.shared

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.presentation.App
import com.dart69.petstore.shared.presentation.ImageLoader
import com.dart69.petstore.shared.presentation.ViewModelFactory
import kotlinx.coroutines.flow.*

fun <T> T.employ(block: T.() -> Unit) = block(this)

infix fun <T, R> T.then(right: R): R = right

fun <T> taskFlowOf(block: suspend () -> T): Flow<Task<T>> = flow<Task<T>> {
    val result = block()
    emit(Task.Completed(result))
}.onStart {
    emit(Task.Loading())
}.catch { throwable ->
    emit(Task.Error(throwable))
}

suspend fun <T> MutableSharedFlow<Task<T>>.emitTasks(resultBlock: suspend () -> T) {
    val upstream = taskFlowOf(resultBlock)
    emitAll(upstream)
}

fun Context.requireFactory(): ViewModelProvider.Factory {
    val app = applicationContext as App
    return ViewModelFactory(
        app.provideAvailableDispatchers(),
        app.provideGetPetItemsUseCase(),
        app.provideGetSelectionDetailsUseCase(),
        app.provideDeleteSinglePetUseCase(),
        app.provideToggleSingleItemSelectedUseCase(),
        app.provideToggleAllPetsSelectedUseCase(),
        app.provideToggleSelectedItemsToFavouriteUseCase(),
        app.provideDeleteSelectedPetsUseCase(),
        app.provideToggleSingleItemFavouriteUseCase(),
        app.provideApplicationScope(),
        app.provideRefreshRepositoryUseCase(),
        app.provideUpdatePetUseCase(),
        app.provideDownloadAvatarUseCase(),
        app.provideObserveMessageUseCase(),
        app.provideSendMessageUseCase(),
        app.provideLoadPreviousUseCase(),
        app.provideLoadNextUseCase()
    )
}

fun Fragment.requireFactory(): ViewModelProvider.Factory =
    requireContext().requireFactory()

fun Fragment.requireImageLoader(): ImageLoader =
    (requireContext().applicationContext as App).provideImageLoader()

fun Fragment.getDrawable(@DrawableRes res: Int): Drawable? =
    AppCompatResources.getDrawable(requireContext(), res)

fun View.showPopupMenu(@MenuRes menuRes: Int, itemClickListener: (Int) -> Boolean) {
    PopupMenu(context, this).apply {
        inflate(menuRes)
        setOnMenuItemClickListener { itemClickListener(it.itemId) }
    }.show()
}

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

var View.isEnabledAlpha: Boolean
    get() = isEnabled
    set(enabled) {
        alpha = if (enabled) 1.0f else 0.5f
        isEnabled = enabled
    }

inline fun <T : Any> Iterable<T>.itemBefore(predicate: (T) -> Boolean): T? {
    var previous: T? = null
    forEach { current ->
        if(predicate(current)) return previous
        previous = current
    }
    return null
}

inline fun <T: Any> Iterable<T>.itemAfter(predicate: (T) -> Boolean): T? {
    val currentIterator = iterator()
    while (currentIterator.hasNext()) {
        val current = currentIterator.next()
        if(predicate(current) && currentIterator.hasNext()) return currentIterator.next()
    }
    return null
}