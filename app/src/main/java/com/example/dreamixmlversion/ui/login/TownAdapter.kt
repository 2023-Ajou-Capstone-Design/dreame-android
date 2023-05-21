package com.example.dreamixmlversion.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.databinding.ItemTownBinding

class TownAdapter(
    private val onItemClicked: (String) -> Unit
): ListAdapter<String, TownAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemTownBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.regionNameTextView.text = item

            binding.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TownAdapter.ViewHolder =
        ViewHolder(ItemTownBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object: ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

}