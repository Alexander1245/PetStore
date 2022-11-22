package com.dart69.petstore.presentation.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dart69.petstore.databinding.PetItemBinding
import com.dart69.petstore.model.extensions.use
import com.dart69.petstore.model.item.ListItem
import com.dart69.petstore.model.item.Pet
import com.dart69.petstore.presentation.utils.ImageLoader
import com.dart69.petstore.presentation.utils.SelectableAdapter


typealias PetItem = ListItem.Implementation<Long, Pet>
typealias SelectablePetViewHolder = SelectableAdapter.SelectableViewHolder<Long, PetItem, PetItemBinding>

class PetAdapter(
    private val imageLoader: ImageLoader,
    private val callbacks: ItemCallbacks.PetCallbacks<Long, PetItem>
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
        private val callbacks: ItemCallbacks.PetCallbacks<Long, PetItem>,
        private val imageLoader: ImageLoader
    ) : SelectablePetViewHolder(binding) {
        override fun bind(item: PetItem) = binding.use {
            val position = absoluteAdapterPosition
            callbacks.highlight(binding.root, item)
            textViewName.text = item.name
            textViewDetails.text = item.details
            checkBoxIsFavourite.isChecked = item.isFavourite
            checkBoxIsFavourite.setOnClickListener { callbacks.onFavouriteClick(item, position) }
            itemView.setOnClickListener { callbacks.onItemViewClick(item, position) }
            itemView.setOnLongClickListener { callbacks.onItemViewLongClick(item, position) }
            imageViewAvatar.setOnClickListener { callbacks.onAvatarClick(item, position) }
            imageViewDelete.setOnClickListener { callbacks.onDeleteClick(item, position) }
            imageViewMore.setOnClickListener { callbacks.onMoreClick(item, position, it) }
            imageLoader.loadInto(item.avatarUri, imageViewAvatar)
        }
    }
}