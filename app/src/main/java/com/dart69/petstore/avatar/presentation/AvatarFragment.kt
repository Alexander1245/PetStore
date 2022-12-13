package com.dart69.petstore.avatar.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dart69.petstore.databinding.FragmentAvatarBinding
import com.dart69.petstore.shared.employ
import com.dart69.petstore.shared.isEnabledAlpha
import com.dart69.petstore.shared.presentation.Screen
import com.dart69.petstore.shared.requireFactory
import com.dart69.petstore.shared.requireImageLoader

class AvatarFragment : Fragment(), Screen {
    private lateinit var binding: FragmentAvatarBinding
    private val viewModel by viewModels<AvatarViewModel> { requireFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.employ {
        super.onViewCreated(view, savedInstanceState)
        val args by navArgs<AvatarFragmentArgs>()
        viewModel.initialize(args.pet)
        viewModel.screenState.collectWhenStarted { screenState ->
            requireImageLoader().loadInto(screenState.avatarUri, imageViewAvatar)
            toolbar.apply {
                title = screenState.title
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            buttonNext.isEnabledAlpha = screenState.isNextEnabled
            buttonPrevious.isEnabledAlpha = screenState.isPreviousEnabled
            progressBar.isVisible = screenState.isInProgress
        }
        buttonNext.setOnClickListener { viewModel.loadNext() }
        buttonPrevious.setOnClickListener { viewModel.loadPrevious() }
    }
}