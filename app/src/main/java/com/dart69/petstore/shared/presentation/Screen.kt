package com.dart69.petstore.shared.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface Screen : LifecycleOwner {
    fun <T> Flow<T>.collectWhenStarted(collector: FlowCollector<T>) {
        val scope = if (this@Screen is Fragment) viewLifecycleOwner.lifecycleScope else lifecycleScope
        val lifecycle = if (this@Screen is Fragment) viewLifecycleOwner.lifecycle else lifecycle
        scope.launch {
            flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect(collector)
        }
    }
}