package com.dart69.petstore.main.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dart69.petstore.R
import com.dart69.petstore.databinding.ActivityMainBinding
import com.dart69.petstore.shared.presentation.Screen
import com.dart69.petstore.shared.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Screen {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navigationHostFragment))
        viewModel.messages.collectWhenStarted { message ->
            showToast(message)
        }
    }
}