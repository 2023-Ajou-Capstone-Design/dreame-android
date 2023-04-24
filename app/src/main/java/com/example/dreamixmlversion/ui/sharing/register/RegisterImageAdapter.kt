package com.example.dreamixmlversion.ui.sharing

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.databinding.ItemSharingImageAttchedBinding

class RegisterImageAdapter(
    private val deleteImageClickListener: (SharingImageItem) -> Unit
) : ListAdapter<SharingImageItem, RegisterImageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemSharingImageAttchedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sharingImageItem: SharingImageItem) {
            with(binding) {
                previewImageView.setImageURI(sharingImageItem.uri)
                deleteImageButton.setOnClickListener {
                    deleteImageClickListener(sharingImageItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterImageAdapter.ViewHolder =
        ViewHolder(ItemSharingImageAttchedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RegisterImageAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SharingImageItem>() {
            override fun areItemsTheSame(
                oldItem: SharingImageItem,
                newItem: SharingImageItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SharingImageItem,
                newItem: SharingImageItem
            ): Boolean {
                return oldItem.uri == newItem.uri
            }

        }
    }
}

data class SharingImageItem(
    val uri: Uri
)