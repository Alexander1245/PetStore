package com.dart69.petstore.shared.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {
    protected fun <T> Flow<T>.collectWhenStarted(collector: FlowCollector<T>) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect(collector)
        }
    }
}