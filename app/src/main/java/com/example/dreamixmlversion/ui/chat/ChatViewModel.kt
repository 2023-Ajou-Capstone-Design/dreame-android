package com.example.dreamixmlversion.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.ChatRepository
import com.example.dreamixmlversion.extension.convertBase64ToStr
import com.example.dreamixmlversion.extension.convertStrToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _chatRoomId = MutableLiveData("")
    private val _otherUserId = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            chatRepository.getChatRoomList(getUserId())
        }
    }

    val myUserId = preferenceManager.getDreameEmailAddress().toString()

    fun setChatRoomId(chatRoomId: String) {
        _chatRoomId.value = chatRoomId
    }

    fun setOtherUserId(otherUserId: String) {
        _otherUserId.value = otherUserId
    }

    fun getOtherUserId() = _otherUserId.value.toString()

    private fun getUserId(): String {
        return preferenceManager.getDreameEmailAddress().toString()
    }

    val chatRoomList = chatRepository.chatRooms

    fun addMessage(isChatRoom: Boolean, messageContent: String) {
        viewModelScope.launch {
            chatRepository.addMessage(
                otherUserId = _otherUserId.value.toString(),
                isExistChatRoom = isChatRoom,
                myUserId = getUserId(),
                chatRoomId = _chatRoomId.value.toString(),
                messageContent = messageContent
            )
        }
    }

    val chatMessageList = chatRepository.messages
    fun getChatMessageList() {
        viewModelScope.launch {
            chatRepository.getChatMessageList(
                getUserId(),
                _otherUserId.value.toString(),
                _chatRoomId.value.toString()
            )
        }
    }

    fun removeEventListenerFromMessages() {
        viewModelScope.launch {
            chatRepository.removeEventListenerFromMessages(_chatRoomId.value.toString())
        }
    }

    companion object {
        const val LIST = 0
        const val DETAIL = 1

    }
}