package com.example.dreamixmlversion.ui.sharing.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.databinding.ItemSharingBinding
import com.example.dreamixmlversion.extension.convertToBitmap
import com.example.dreamixmlversion.extension.decodeForBase64

class SharingAdapter(
    private val sharingItemClickListener: (SharingDataItemEntity) -> Unit
) : ListAdapter<SharingDataItemEntity, SharingAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemSharingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sharingItem: SharingDataItemEntity) {
            with(binding) {

                if (sharingItem.thumbnailImage.isNotEmpty()) {
                    sharingThumbnailImageView.setImageBitmap(sharingItem.thumbnailImage.decodeForBase64())
                }
                sharingTitleTextView.text = sharingItem.title
                sharingAddressTextView.text = sharingItem.town
                sharingTimeTextView.text = sharingItem.uploadTime
                root.setOnClickListener {
                    sharingItemClickListener(sharingItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSharingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SharingDataItemEntity>() {
            override fun areItemsTheSame(
                oldItem: SharingDataItemEntity,
                newItem: SharingDataItemEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SharingDataItemEntity,
                newItem: SharingDataItemEntity
            ): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}