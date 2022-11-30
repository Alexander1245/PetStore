package com.dart69.petstore.shared.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment() {
    protected fun <T> Flow<T>.collectWhenStarted(collector: FlowCollector<T>) {
        viewLifecycleOwner.lifecycleScope.launch {
            flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect(collector)
        }
    }
}