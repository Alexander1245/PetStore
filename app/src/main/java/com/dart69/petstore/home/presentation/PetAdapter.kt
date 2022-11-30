package com.dart69.petstore.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dart69.petstore.R
import com.dart69.petstore.databinding.PetItemBinding
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.shared.model.item.ListItem
import com.dart69.petstore.shared.presentation.ImageLoader
import com.dart69.petstore.shared.presentation.SelectableAdapter
import com.dart69.petstore.shared.showPopupMenu
import com.dart69.petstore.shared.use
import com.google.android.material.color.MaterialColors

typealias PetItem = ListItem.Implementation<Long, Pet>
typealias SelectablePetViewHolder = SelectableAdapter.SelectableViewHolder<Long, PetItem, PetItemBinding>

class PetAdapter(
    private val imageLoader: ImageLoader,
    private val callbacks: PetAdapterCallbacks
) : SelectableAdapter<Long, PetItem, PetItemBinding, PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PetItemBinding.inflate(inflater, parent, false)
        return PetViewHolder(binding, callbacks, imageLoader)
    }

    override fun onBindViewHolder(
        holder: SelectableViewHolder<Long, PetItem, PetItemBinding>,
        position: Int
    ) = holder.bind(currentList[position])

    class PetViewHolder(
        private val binding: PetItemBinding,
        private val callbacks: PetAdapterCallbacks,
        private val imageLoader: ImageLoader
    ) : SelectablePetViewHolder(binding) {
        override fun bind(item: PetItem) = binding.use {
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