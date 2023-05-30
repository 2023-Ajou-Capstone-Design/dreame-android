package com.example.dreamixmlversion.ui.chat.chatdetail

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.databinding.FragmentChatroomDetailBinding
import com.example.dreamixmlversion.ui.chat.ChatBaseFragment
import com.example.dreamixmlversion.ui.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatDetailFragment : ChatBaseFragment<FragmentChatroomDetailBinding>() {

    private lateinit var chatMessageAdapter: ChatMessageAdapter

    override fun getViewBinding(): FragmentChatroomDetailBinding =
        FragmentChatroomDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackButtonClicked()
        initChatMessageList()
        initMessageInputView()
        initSendMessageButton()
        setOtherNickname()
        getMessages()
        setChatRoomTitle()
    }

    private fun initChatMessageList() {
        chatMessageAdapter = ChatMessageAdapter(chatViewModel.myNickname)
        _binding?.chatMessageRecyclerView?.apply {
            adapter = chatMessageAdapter
        }

        chatMessageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                _binding?.chatMessageRecyclerView?.layoutManager?.smoothScrollToPosition(
                    _binding?.chatMessageRecyclerView,
                    null,
                    chatMessageAdapter.itemCount
                )
            }
        })

        lifecycleScope.launch {
            chatViewModel.chatMessageList.collect {
                chatMessageAdapter.submitList(it)
            }
        }
    }

    private fun initMessageInputView() {
        _binding?.messageInputEditTextView?.addTextChangedListener {
            _binding?.sendButton?.isClickable = _binding?.messageInputEditTextView?.length()!! > 1
//            _binding?.sendButton?.isEnabled
        }
    }

    private fun initSendMessageButton() {
        _binding?.sendButton?.setOnClickListener {
            val isChatRoom = chatMessageAdapter.currentList.size != 0
            val messageContent = _binding?.messageInputEditTextView?.text.toString()
            chatViewModel.addMessage(isChatRoom, messageContent)
            _binding?.messageInputEditTextView?.text = null
            if (chatMessageAdapter.currentList.size == 0) {
                getMessages()
            }
        }
    }

    private fun setOtherNickname() {
        val otherUserId = arguments?.getString("otherNickname").toString()
        if (otherUserId.isNotEmpty()) {
            chatViewModel.setOtherNickname(otherUserId)
        }
    }

    private fun getMessages() {
        chatViewModel.getChatMessageList()
    }

    private fun setChatRoomTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            chatViewModel.getOtherUserId()
    }

    private fun onBackButtonClicked() {
        requireActivity().onBackPressedDispatcher.addCallback {
            chatViewModel.removeEventListenerFromMessages()
            findNavController().popBackStack()
        }
    }
}
