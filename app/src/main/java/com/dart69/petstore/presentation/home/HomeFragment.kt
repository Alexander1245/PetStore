package com.dart69.petstore.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dart69.petstore.R
import com.dart69.petstore.databinding.FragmentHomeBinding
import com.dart69.petstore.model.Progress
import com.dart69.petstore.model.extensions.use
import com.dart69.petstore.presentation.extensions.provideFactory
import com.dart69.petstore.presentation.extensions.provideImageLoader
import com.dart69.petstore.presentation.home.recyclerview.PetAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels { provideFactory() }
    private val imageLoader by lazy { provideImageLoader() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.use {
        super.onViewCreated(view, savedInstanceState)
        val petAdapter = PetAdapter(imageLoader, viewModel)
        toolbar.setOnMenuItemClickListener {
            viewModel.onToolbarMenuItemClick(it.itemId)
        }
        actionTextView.setOnClickListener {
            viewModel.onActionClick()
        }
        recyclerView.apply {
            adapter = petAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.toolbarItemsVisibility.observe(viewLifecycleOwner) { isItemsVisible ->
            toolbar.menu.forEach { it.isVisible = isItemsVisible }
        }
        viewModel.pets.observe(viewLifecycleOwner) { pets ->
            petAdapter.submitList(pets)
        }
        viewModel.messages.observe(viewLifecycleOwner) { event ->
            val message = event.getContentIfNotHandled() ?: return@observe
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.selection.observe(viewLifecycleOwner) { selection ->
            actionsGroup.isVisible = selection.selected > 0
            actionHintTextView.text =
                getString(R.string.selected_items_hint, selection.selected, selection.total)
        }
        viewModel.actionText.observe(viewLifecycleOwner) { textRes ->
            actionTextView.setText(textRes)
        }
        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            updateProgressUi(progress)
            //Receive error messages and show them
        }
    }

    private fun updateProgressUi(progress: Progress) = binding.use {
        val include = progressLayout.root
        val isCompleted = progress is Progress.Completed
        val isError = progress is Progress.Error
        recyclerView.isVisible = isCompleted
        include.isVisible = !isCompleted
        include.forEach {
            if (it.id == R.id.buttonTryAgain || it.id == R.id.textViewWaitHint) {
                it.isVisible = isError
            }
        }
    }
}