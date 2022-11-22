package com.dart69.petstore.presentation.home

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.dart69.petstore.R
import com.dart69.petstore.data.repository.FavouriteItemRepository
import com.dart69.petstore.model.Progress
import com.dart69.petstore.model.SelectionState
import com.dart69.petstore.model.extensions.then
import com.dart69.petstore.model.item.Pet
import com.dart69.petstore.presentation.extensions.map
import com.dart69.petstore.presentation.extensions.updateLastValue
import com.dart69.petstore.presentation.home.recyclerview.PetItem
import com.dart69.petstore.presentation.utils.LiveEvent
import com.dart69.petstore.presentation.utils.MutableLiveEvent
import com.dart69.petstore.presentation.utils.eventValue
import com.dart69.petstore.presentation.utils.selection.Tracker
import kotlinx.coroutines.launch

inline fun <T, reified R : MutableLiveData<T>> LiveData<T>.mutate(): R = this as R

class HomeViewModel(
    private val repository: FavouriteItemRepository<Long, Pet>,
    private val tracker: Tracker<Long, PetItem>
) : ViewModel() {
    private val rawPets = MutableLiveData<List<Pet>>()
    val toasts: LiveEvent<Int> = MutableLiveEvent()
    val progress: LiveData<Progress> = MutableLiveData()
    val selection: LiveData<SelectionState> =
        MediatorLiveData<SelectionState>().apply { value = SelectionState.EMPTY }
    val actionText: LiveData<Int> = MutableLiveData()
    val toolbarItemsVisibility: LiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val pets = rawPets.map { items ->
        items.map { PetItem(it, tracker.isSelected(it.id)) }
    }

    init {
        val mutableSelection = selection.mutate<SelectionState, MediatorLiveData<SelectionState>>()
        repository.addOnChangedListener { rawPets.postValue(it) }
        mutableSelection.addSource(rawPets) { items ->
            mutableSelection.updateLastValue { state -> state.copy(total = items.size) }
        }
        tracker.addOnChangedListener {
            val selected = tracker.count()
            val total = pets.value!!.size
            val hint = if (selected == total) R.string.unselect_all else R.string.select_all
            actionText.mutate().value = hint
            mutableSelection.updateLastValue { state -> state.copy(selected = selected) }
            //TODO: Optimize computations, make it in pets instead of rawPets
            rawPets.updateLastValue { items -> items.toMutableList() }
            toolbarItemsVisibility.mutate().value = selected > 0
        }
        invokeWithProgress { rawPets.postValue(repository.getAllSortedByFavourite()) }
    }

    fun onItemClick(item: PetItem) {
        if (tracker.hasSelection()) {
            tracker.toggle(item.id)
        } else {
            showToast(R.string.item_view_clicked)
        }
    }

    fun onItemLongClick(item: PetItem) {
        tracker.toggle(item.id)
    }

    fun deleteSelected() {
        invokeWithProgress {
            repository.deleteByKeys(tracker.keys.toList())
            tracker.clear()
        }
    }

    fun toggleFavouriteForSelected() {
        invokeWithProgress {
            val pets =
                repository.findByKeys(tracker.keys.toList()).map { it.toggleFavourite() as Pet }
            repository.updateMany(pets)
        }
    }

    fun onToolbarMenuItemClick(@IdRes id: Int): Boolean = when (id) {
        R.id.itemDeleteSelected -> deleteSelected() then true
        R.id.itemToggleFavouriteForSelected -> toggleFavouriteForSelected() then true
        else -> false
    }

    fun onActionClick() {
        val isAllItemsSelected = tracker.count() == rawPets.value?.size
        val action = if (isAllItemsSelected) tracker::unselectAll else tracker::selectAll
        pets.value?.let { items ->
            action(items.map { it.id })
        }
    }

    fun onPopupActionsClick(item: PetItem, @IdRes id: Int): Boolean = when (id) {
        R.id.itemDelete -> deletePet(item) then true
        R.id.itemToggleFavourite -> togglePetFavourite(item.map()) then true
        R.id.itemToggleSelected -> onItemLongClick(item) then true
        else -> false
    }

    fun deletePet(pet: PetItem) {
        val key = pet.id
        invokeWithProgress {
            repository.delete(pet.map())
            tracker.unselect(key)
        }
    }

    fun togglePetFavourite(pet: Pet) {
        invokeWithProgress { repository.update(pet.toggleFavourite() as Pet) }
    }

    fun showToast(@StringRes messageId: Int) {
        toasts.mutate().eventValue = messageId
    }

    private fun invokeWithProgress(
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            val mutableProgress = progress.mutate()
            try {
                mutableProgress.value = Progress.Active
                block()
                mutableProgress.value = Progress.Completed
            } catch (exception: Exception) {
                mutableProgress.value = Progress.Error(exception)
                showToast(R.string.cant_load_data)
            }
        }
    }
}