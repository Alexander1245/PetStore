package com.dart69.petstore.details.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dart69.petstore.R
import com.dart69.petstore.databinding.FragmentDetailsBinding
import com.dart69.petstore.shared.employ
import com.dart69.petstore.shared.getDrawable
import com.dart69.petstore.shared.presentation.ImageLoader
import com.dart69.petstore.shared.presentation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(), Screen {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsViewModel>()
    @Inject lateinit var imageLoader: ImageLoader

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
        val args by navArgs<DetailsFragmentArgs>()
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