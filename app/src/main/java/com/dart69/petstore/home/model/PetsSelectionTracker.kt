package com.dart69.petstore.home.model

import com.dart69.petstore.shared.model.SelectionTracker

interface PetsSelectionTracker : SelectionTracker<Long, SelectablePet>

fun SelectionTracker<Long, SelectablePet>.asPetsSelectionTracker(): PetsSelectionTracker =
    object : PetsSelectionTracker, SelectionTracker<Long, SelectablePet> by this {}