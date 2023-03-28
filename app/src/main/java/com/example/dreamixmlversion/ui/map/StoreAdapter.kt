package com.example.dreamixmlversion.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem
import com.example.dreamixmlversion.databinding.ItemStoreBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StoreAdapter @Inject constructor(): ListAdapter<StoreDataEntityItem, StoreAdapter.ViewHolder>(diffUtil) {

    private lateinit var onStoreClickListener: (StoreDataEntityItem) -> Unit
//    private val favoriteClickListener: (StoreDataEntityItem) -> Unit

    inner class ViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(store: StoreDataEntityItem) {
            with(binding) {
                root.setOnClickListener { onStoreClickListener(store) }
//                favoriteButton.setOnClickListener { favoriteClickListener(spot) }

                storeNameTextView.text = store.storeID.toString()
                categoryTextView.text = store.category.toString()
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
        val diffUtil = object: DiffUtil.ItemCallback<StoreDataEntityItem>() {
            override fun areItemsTheSame(
                oldItem: StoreDataEntityItem,
                newItem: StoreDataEntityItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoreDataEntityItem,
                newItem: StoreDataEntityItem
            ): Boolean {
                return oldItem.storeID == newItem.storeID
            }

        }
    }

    fun setStoreClickListener(onStoreItemClickListener: (StoreDataEntityItem) -> Unit) {
        this.onStoreClickListener = onStoreItemClickListener
    }
}