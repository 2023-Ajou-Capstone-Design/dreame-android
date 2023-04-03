package com.example.dreamixmlversion.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.databinding.ItemCategoryBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class CategoryAdapter: ListAdapter<CategoryEntity, CategoryAdapter.ViewHolder>(diffUtil) {

    private lateinit var categoryClickListener: (CategoryEntity) -> Unit

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: CategoryEntity) {
            with(binding) {
                categoryButton.apply {
                    text = store.categoryName
                    setOnClickListener { categoryClickListener(store) }
                }
            }
        }
    }

    fun setOnCategoryClickListener(onCategoryClickListener: (CategoryEntity) -> Unit) {
        this.categoryClickListener = onCategoryClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder =
        ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryEntity>() {
            override fun areItemsTheSame(
                oldItem: CategoryEntity,
                newItem: CategoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CategoryEntity,
                newItem: CategoryEntity
            ): Boolean {
                return oldItem.categoryName == newItem.categoryName
            }

        }
    }
}