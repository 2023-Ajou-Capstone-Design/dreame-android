package com.example.dreamixmlversion.ui.sharing.register

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
                val resized = Bitmap.createScaledBitmap(sharingImageItem.bitmapImage, 350, 350, true)
                previewImageView.setImageBitmap(resized)
                deleteImageButton.setOnClickListener {
                    deleteImageClickListener(sharingImageItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSharingImageAttchedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: SharingImageItem,
                newItem: SharingImageItem
            ): Boolean {
                return oldItem.bitmapImage == newItem.bitmapImage
            }
        }
    }
}

data class SharingImageItem(
    val bitmapImage: Bitmap
)