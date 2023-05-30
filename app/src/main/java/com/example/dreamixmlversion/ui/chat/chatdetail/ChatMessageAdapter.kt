package com.example.dreamixmlversion.ui.chat.chatdetail

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.data.api.response.model.chat.ChatMessageModel
import com.example.dreamixmlversion.databinding.ItemChatroomDetailMyMessageBinding
import com.example.dreamixmlversion.databinding.ItemChatroomDetailOpponentMessageBinding

class ChatMessageAdapter(private val nickname: String) :
    ListAdapter<ChatMessageModel, RecyclerView.ViewHolder>(diffUtil) {

    inner class MyViewHolder(private val binding: ItemChatroomDetailMyMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatMessageModel) {
            binding.messageTextView.text = item.messageContent.toString()
        }
    }

    inner class OpponentViewHolder(private val binding: ItemChatroomDetailOpponentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatMessageModel) {
            val index = currentList.indexOf(item) - 1
            if (index >= 0 && getItemViewType(index) == OPPONENT) {
                binding.profileImageView.visibility = View.GONE
                binding.nicknameTextView.visibility = View.GONE
//                binding.messageTextView.setMargins(0, 0, 0, 0)
            } else {
                binding.profileImageView.visibility = View.VISIBLE
                binding.nicknameTextView.visibility = View.VISIBLE
                binding.nicknameTextView.text = item.nickname.toString()
//                binding.messageTextView.setMargins(0, 0, 0, 0)
            }
            binding.messageTextView.text = item.messageContent.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].nickname == this.nickname) MY else OPPONENT
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return when (viewType) {
            MY -> {
                val binding = ItemChatroomDetailMyMessageBinding.inflate(inflater, parent, false)
                MyViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemChatroomDetailOpponentMessageBinding.inflate(inflater, parent, false)
                OpponentViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                holder.bind(currentList[position])
            }
            is OpponentViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatMessageModel>() {
            override fun areItemsTheSame(
                oldItem: ChatMessageModel,
                newItem: ChatMessageModel
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ChatMessageModel,
                newItem: ChatMessageModel
            ): Boolean {
                return oldItem.uploadTime == newItem.uploadTime
            }
        }

        const val MY = 0
        const val OPPONENT = 1
    }

}