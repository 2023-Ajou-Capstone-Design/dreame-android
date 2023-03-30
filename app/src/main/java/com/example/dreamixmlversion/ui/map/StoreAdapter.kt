package com.example.dreamixmlversion.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.entity.StoreDataByCategoryClicked
import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.databinding.ItemStoreBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StoreAdapter @Inject constructor(): ListAdapter<StoreDataByCategoryClicked, StoreAdapter.ViewHolder>(diffUtil) {

    private lateinit var onStoreClickListener: (StoreDataByCategoryClicked) -> Unit
//    private val favoriteClickListener: (StoreDataEntityItem) -> Unit

    inner class ViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(store: StoreDataByCategoryClicked) {
            with(binding) {
                root.setOnClickListener { onStoreClickListener(store) }
//                favoriteButton.setOnClickListener { favoriteClickListener(spot) }

                storeNameTextView.text = store.storeName
                categoryTextView.text = store.cateName
                storeOpeningTextView.text = "미정"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder =
        ViewHolder(ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<StoreDataByCategoryClicked>() {
            override fun areItemsTheSame(
                oldItem: StoreDataByCategoryClicked,
                newItem: StoreDataByCategoryClicked
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoreDataByCategoryClicked,
                newItem: StoreDataByCategoryClicked
            ): Boolean {
                return oldItem.storeId == newItem.storeId
            }

        }
    }

    fun setOnStoreClickListener(onStoreItemClickListener: (StoreDataByCategoryClicked) -> Unit) {
        this.onStoreClickListener = onStoreItemClickListener
    }
}