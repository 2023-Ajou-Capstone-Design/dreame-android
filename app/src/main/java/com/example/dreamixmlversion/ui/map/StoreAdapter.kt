package com.example.dreamixmlversion.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.databinding.ItemStoreBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StoreAdapter @Inject constructor(): ListAdapter<StoreDataOnBottomSheetList, StoreAdapter.ViewHolder>(diffUtil) {

    private lateinit var onStoreClickListener: (StoreDataOnBottomSheetList) -> Unit
//    private val favoriteClickListener: (StoreDataEntityItem) -> Unit

    inner class ViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(store: StoreDataOnBottomSheetList) {
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
        val diffUtil = object: DiffUtil.ItemCallback<StoreDataOnBottomSheetList>() {
            override fun areItemsTheSame(
                oldItem: StoreDataOnBottomSheetList,
                newItem: StoreDataOnBottomSheetList
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoreDataOnBottomSheetList,
                newItem: StoreDataOnBottomSheetList
            ): Boolean {
                return oldItem.storeId == newItem.storeId
            }

        }
    }

    fun setOnStoreClickListener(onStoreItemClickListener: (StoreDataOnBottomSheetList) -> Unit) {
        this.onStoreClickListener = onStoreItemClickListener
    }
}