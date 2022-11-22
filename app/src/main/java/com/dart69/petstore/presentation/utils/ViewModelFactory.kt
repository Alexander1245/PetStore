package com.dart69.petstore.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dart69.petstore.model.item.Pet
import com.dart69.petstore.data.repository.FavouriteItemRepository
import com.dart69.petstore.presentation.home.HomeViewModel
import com.dart69.petstore.presentation.home.recyclerview.PetItem
import com.dart69.petstore.presentation.utils.selection.Tracker

class ViewModelFactory(
    private val repository: FavouriteItemRepository<Long, Pet>,
    private val selectionTracker: Tracker<Long, PetItem>
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            HomeViewModel::class.java -> HomeViewModel(repository, selectionTracker)
            else -> error("Can't find actual viewModel.")
        }
        return viewModel as T
    }
}