package com.dart69.petstore.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dart69.petstore.R
import com.dart69.petstore.databinding.FragmentHomeBinding
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.onComplete
import com.dart69.petstore.shared.presentation.BaseFragment
import com.dart69.petstore.shared.provideFactory
import com.dart69.petstore.shared.provideImageLoader
import com.dart69.petstore.shared.use
import com.google.android.material.snackbar.Snackbar

class HomeFragment : BaseFragment() {
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
            viewModel.onGroupActionItemClick(it.itemId)
        }
        actionButton.setOnClickListener {
            viewModel.onActionButtonClick()
        }
        recyclerView.apply {
            adapter = petAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.pets.collectWhenStarted { task ->
            progressLayout.root.isVisible = task !is Task.Completed
            recyclerView.isVisible = task is Task.Completed
            task.onComplete { pets ->
                petAdapter.submitList(pets)
                emptyPetsTextView.isVisible = pets.isEmpty()
            }
        }
        viewModel.messages.collectWhenStarted { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.selectionDetails.collectWhenStarted { details ->
            actionHintTextView.text =
                getString(R.string.selected_items_hint, details.selected, details.total)
        }
        viewModel.groupActionsVisibility.collectWhenStarted { visibility ->
            toolbar.menu.forEach { it.isVisible = visibility == View.VISIBLE }
            actionGroup.visibility = visibility
        }
        viewModel.actionHint.collectWhenStarted { hintRes ->
            actionButton.setText(hintRes)
        }
    }
}