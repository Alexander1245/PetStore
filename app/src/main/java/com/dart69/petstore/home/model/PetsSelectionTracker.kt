package com.dart69.petstore.home.model

import com.dart69.petstore.shared.model.SelectionTracker

interface PetsSelectionTracker : SelectionTracker<Long, Pet>

fun SelectionTracker<Long, Pet>.asPetsSelectionTracker() : PetsSelectionTracker =
    object : PetsSelectionTracker, SelectionTracker<Long, Pet> by this {}