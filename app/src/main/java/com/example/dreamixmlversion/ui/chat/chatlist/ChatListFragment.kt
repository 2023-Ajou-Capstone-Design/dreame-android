package com.example.dreamixmlversion.ui.chat.chatlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentChatBinding
import com.example.dreamixmlversion.ui.chat.ChatBaseFragment
import com.example.dreamixmlversion.ui.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListFragment : ChatBaseFragment<FragmentChatBinding>() {

    private lateinit var chatListAdapter: ChatListAdapter

    override fun getViewBinding(): FragmentChatBinding =
        FragmentChatBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChatRoomList()
    }

    private fun initChatRoomList() {
        chatListAdapter = ChatListAdapter { item ->
            chatViewModel.setOtherNickname(item.otherNickname.toString())
            chatViewModel.setChatRoomId(item.chatRoomId.toString())
            findNavController().navigate(R.id.action_chat_screen_to_detail_screen)
        }

        _binding?.chatRoomRecyclerView?.adapter = chatListAdapter

        lifecycleScope.launch {
            chatViewModel.chatRoomList.collect {
                chatListAdapter.submitList(it)
            }
        }
    }
}