package com.dart69.petstore.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dart69.petstore.R
import com.dart69.petstore.databinding.PetItemBinding
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.presentation.ImageLoader
import com.dart69.petstore.shared.presentation.ItemAdapter
import com.dart69.petstore.shared.presentation.ItemViewHolder
import com.dart69.petstore.shared.showPopupMenu
import com.dart69.petstore.shared.employ
import com.google.android.material.color.MaterialColors

typealias PetItemViewHolder = ItemViewHolder<Long, SelectablePet, PetItemBinding>

class PetAdapter(
    private val imageLoader: ImageLoader,
    private val callbacks: PetAdapterCallbacks
) : ItemAdapter<Long, SelectablePet, PetItemBinding, PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PetItemBinding.inflate(inflater, parent, false)
        return PetViewHolder(binding, callbacks, imageLoader)
    }

    override fun onBindViewHolder(
        holder: PetItemViewHolder,
        position: Int
    ) = holder.bind(currentList[position])

    class PetViewHolder(
        private val binding: PetItemBinding,
        private val callbacks: PetAdapterCallbacks,
        private val imageLoader: ImageLoader
    ) : PetItemViewHolder(binding) {
        override fun bind(item: SelectablePet) = binding.employ {
            val context = itemView.context
            val backgroundColor = if (item.isSelected) {
                ContextCompat.getColor(context, R.color.purple_200)
            } else {
                MaterialColors.getColor(itemView, R.attr.colorOnPrimary)
            }
            binding.root.setBackgroundColor(backgroundColor)
            textViewName.text = item.name
            textViewDetails.text = item.details
            checkBoxIsFavourite.isChecked = item.isFavourite
            checkBoxIsFavourite.setOnClickListener { callbacks.onFavouriteClick(item) }
            itemView.setOnClickListener { callbacks.onItemViewClick(item) }
            itemView.setOnLongClickListener { callbacks.onItemViewLongClick(item) }
            imageViewAvatar.setOnClickListener { callbacks.onAvatarClick(item) }
            imageViewDelete.setOnClickListener { callbacks.onDeleteClick(item) }
            imageViewMore.setOnClickListener {
                it.showPopupMenu(R.menu.pet_item_popup_menu) { menuItemId ->
                    callbacks.onPopupMenuItemsClick(item, menuItemId)
                }
            }
            imageLoader.loadInto(item.avatarUri, imageViewAvatar)
        }
    }
}