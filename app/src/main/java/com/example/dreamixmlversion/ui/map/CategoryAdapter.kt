package com.example.dreamixmlversion.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.databinding.ItemCategoryBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CategoryAdapter @Inject constructor(): ListAdapter<CategoryItem, CategoryAdapter.ViewHolder>(diffUtil) {

    private lateinit var categoryClickListener: (CategoryItem) -> Unit

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: CategoryItem) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder =
        ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(
                oldItem: CategoryItem,
                newItem: CategoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CategoryItem,
                newItem: CategoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }
}

data class CategoryItem(
    val name: String
)