package com.dart69.petstore.presentation.home

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.dart69.petstore.R
import com.dart69.petstore.data.repository.FavouriteItemRepository
import com.dart69.petstore.model.Progress
import com.dart69.petstore.model.SelectionState
import com.dart69.petstore.model.extensions.then
import com.dart69.petstore.model.item.Pet
import com.dart69.petstore.presentation.home.recyclerview.ItemCallbacks
import com.dart69.petstore.presentation.home.recyclerview.PetItem
import com.dart69.petstore.presentation.utils.BaseViewModel
import com.dart69.petstore.presentation.utils.MessageSender
import com.dart69.petstore.presentation.utils.ResourceManager
import com.dart69.petstore.presentation.utils.selection.Tracker
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FavouriteItemRepository<Long, Pet>,
    private val tracker: Tracker<Long, PetItem>,
    private val resourceManager: ResourceManager
) : BaseViewModel(), ItemCallbacks.PetCallbacks<Long, PetItem>, MessageSender {
    private val rawPets = liveDataOf(emptyList<Pet>())
    val progress = liveDataOf<Progress>(Progress.Completed)
    val toolbarItemsVisibility = liveDataOf(false)
    val actionText = liveDataOf(R.string.select_all)
    val messages = liveEventOf(resourceManager.getString(R.string.loading_data))
    val selection = rawPets.map(SelectionState.EMPTY) { items, currentState ->
        currentState.copy(total = items.size)
    }
    val pets = rawPets.map(emptyList<PetItem>()) { items, _ ->
        items.map { PetItem(it, tracker.isSelected(it.id)) }
    }

    init {
        repository.addOnChangedListener { rawPets.updateValue(it) }
        tracker.addOnChangedListener {
            val selected = tracker.count()
            val total = pets.value().size
            val hint = if (selected == total) R.string.unselect_all else R.string.select_all
            actionText.updateValue(hint)
            toolbarItemsVisibility.updateValue(selected > 0)
            selection.modifyValue { state -> state.copy(selected = selected) }
            rawPets.modifyValue { items -> items.toMutableList() }
        }
        invokeWithProgress { rawPets.updateValue(repository.getAllSortedByFavourite()) }
    }

    override fun onItemViewClick(item: PetItem) {
        if (tracker.hasSelection()) {
            toggle(item)
        } else {
            sendMessage(R.string.item_view_clicked)
        }
    }

    override fun onItemViewLongClick(item: PetItem): Boolean {
        toggle(item)
        return true
    }

    override fun onAvatarClick(item: PetItem) {
        sendMessage(R.string.not_yet_implemented)
    }

    override fun onPopupMenuItemsClick(item: PetItem, @IdRes itemId: Int): Boolean = when (itemId) {
        R.id.itemDelete -> onDeleteClick(item) then true
        R.id.itemToggleFavourite -> onFavouriteClick(item) then true
        R.id.itemToggleSelected -> toggle(item) then true
        else -> false
    }

    override fun onDeleteClick(item: PetItem) = invokeWithProgress {
        val key = item.id
        repository.delete(item.map())
        tracker.unselect(key)
    }

    override fun onFavouriteClick(item: PetItem) = invokeWithProgress {
        val pet = item.map<Pet>()
        repository.update(pet.toggleFavourite() as Pet)
    }

    override fun sendMessage(text: String) {
        messages.updateEventValue(text)
    }

    override fun sendMessage(@StringRes textRes: Int) {
        sendMessage(resourceManager.getString(textRes))
    }

    fun onToolbarMenuItemClick(@IdRes id: Int): Boolean = when (id) {
        R.id.itemDeleteSelected -> deleteSelected() then true
        R.id.itemToggleFavouriteForSelected -> toggleFavouriteForSelected() then true
        else -> false
    }

    fun onActionClick() {
        val allItemsSelected = tracker.count() == rawPets.value().size
        val action = if (allItemsSelected) tracker::unselectAll else tracker::selectAll
        action(pets.value().map { it.id })
    }

    private fun toggle(item: PetItem) {
        tracker.toggle(item.id)
    }

    private fun deleteSelected() = invokeWithProgress {
        repository.deleteByKeys(tracker.keys.toList())
        tracker.clear()
    }

    private fun toggleFavouriteForSelected() = invokeWithProgress {
        val selectedPets = repository.findByKeys(tracker.keys.toList())
        val allFavourites = selectedPets.all { it.isFavourite }
        val mapper = if(allFavourites) Pet::unmakeFavourite else Pet::makeFavourite
        val newPets = selectedPets.map { pet -> mapper(pet) }
        repository.updateMany(newPets)
    }

    private fun invokeWithProgress(
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                progress.updateValue(Progress.Active)
                block()
                progress.updateValue(Progress.Completed)
            } catch (exception: Exception) {
                progress.updateValue(Progress.Error(exception))
            }
        }
    }
}