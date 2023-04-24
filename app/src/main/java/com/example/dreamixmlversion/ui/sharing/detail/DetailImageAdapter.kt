package com.example.dreamixmlversion.ui.sharing.detail

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.databinding.ItemDetailFrameBinding

class DetailImageAdapter(
    private val list: List<DetailImageItem>
) : ListAdapter<DetailImageItem, DetailImageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemDetailFrameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sharingImageItem: DetailImageItem) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageAdapter.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemDetailFrameBinding.inflate(inflater, parent, false)
        return this.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailImageAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DetailImageItem>() {
            override fun areItemsTheSame(
                oldItem: DetailImageItem,
                newItem: DetailImageItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DetailImageItem,
                newItem: DetailImageItem
            ): Boolean {
                return oldItem.uri == newItem.uri
            }

        }
    }
}

data class DetailImageItem(
    val uri: Uri
)