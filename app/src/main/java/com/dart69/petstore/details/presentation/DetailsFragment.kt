package com.dart69.petstore.details.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dart69.petstore.R
import com.dart69.petstore.databinding.FragmentDetailsBinding
import com.dart69.petstore.shared.getDrawable
import com.dart69.petstore.shared.presentation.BaseFragment
import com.dart69.petstore.shared.provideFactory
import com.dart69.petstore.shared.provideImageLoader
import com.dart69.petstore.shared.employ

class DetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsViewModel> { provideFactory() }
    private val imageLoader by lazy { provideImageLoader() }
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.employ {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize(args.pet)
        viewModel.screenState.collectWhenStarted { screenState ->
            imageLoader.loadInto(screenState.avatarUri, imageViewAvatar)
            toolbar.apply {
                title = screenState.titleText
                menu.findItem(R.id.itemToggleFavourite).icon = getDrawable(screenState.iconResource)
            }
            content.textViewDetails.text = screenState.detailsText
            if (screenState.isReadyToClose) findNavController().popBackStack()
        }
        toolbar.apply {
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener { viewModel.onMenuItemsClick(it.itemId) }
        }
    }
}