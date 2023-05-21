package com.example.dreamixmlversion.ui.chat.chatlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.model.chat.ProcessedChatRoomItem
import com.example.dreamixmlversion.databinding.ItemChatroomBinding
import com.example.dreamixmlversion.extension.convertDateFullToTimestamp

class ChatListAdapter(
    private val chatRoomClickListener: (ProcessedChatRoomItem) -> Unit
): ListAdapter<ProcessedChatRoomItem, ChatListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemChatroomBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProcessedChatRoomItem) {
            binding.otherUserTextView.text = item.otherUserId
            binding.chatRoomLastMessageTextView.text = item.lastMessageContent
            binding.chatLastMessageTimeTextView.text = item.lastUploadTime.toString().convertDateFullToTimestamp()
            if (item.unreadMessageNumber != 0) {
                binding.unreadMessageCountTextView.text = item.unreadMessageNumber.toString()
            } else {
                binding.unreadMessageCountTextView.isVisible = false
            }

            binding.root.setOnClickListener {
                chatRoomClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder =
        ViewHolder(ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProcessedChatRoomItem>() {
            override fun areItemsTheSame(
                oldItem: ProcessedChatRoomItem,
                newItem: ProcessedChatRoomItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ProcessedChatRoomItem,
                newItem: ProcessedChatRoomItem
            ): Boolean {
                return oldItem.otherUserId == newItem.otherUserId
            }

        }
    }
}