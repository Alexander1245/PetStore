package com.dart69.petstore.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dart69.petstore.R
import com.dart69.petstore.databinding.FragmentHomeBinding
import com.dart69.petstore.shared.model.*
import com.dart69.petstore.shared.presentation.BaseFragment
import com.dart69.petstore.shared.presentation.provideNavOptions
import com.dart69.petstore.shared.provideFactory
import com.dart69.petstore.shared.provideImageLoader
import com.dart69.petstore.shared.employ

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.employ {
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
            progressGroup.isVisible = task.isNotCompleted()
            recyclerView.isVisible = task.isCompleted()
            task
                .onComplete { pets ->
                    petAdapter.submitList(pets)
                    emptyPetsTextView.isVisible = pets.isEmpty()
                }.onError { throwable ->
                    messageTextView.text =
                        getString(R.string.internal_error_message, throwable.message)
                }.onLoading {
                    messageTextView.text = getString(R.string.loading_data)
                }
        }
        viewModel.selectionDetails.collectWhenStarted { details ->
            actionHintTextView.text =
                getString(R.string.selected_items_hint, details.selected, details.total)
        }
        viewModel.isGroupActionsVisible.collectWhenStarted { isVisible ->
            toolbar.menu.forEach { it.isVisible = isVisible }
            actionsLayout.isVisible = isVisible
        }
        viewModel.actionHint.collectWhenStarted { hintRes ->
            actionButton.setText(hintRes)
        }
        viewModel.navigationDestination.collectWhenStarted { destination ->
            findNavController().navigate(destination, provideNavOptions())
        }
    }
}