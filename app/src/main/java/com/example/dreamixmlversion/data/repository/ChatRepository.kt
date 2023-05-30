package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.model.chat.ChatMessageModel
import com.example.dreamixmlversion.data.api.response.model.chat.ProcessedChatRoomItem
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val chatRooms: Flow<List<ProcessedChatRoomItem>>

    suspend fun getChatRoomList(nickname: String)

    suspend fun sendMessage(
        otherNickname: String? = null,
        isExistChatRoom: Boolean,
        myNickname: String,
        chatRoomId: String,
        messageContent: String
    )

    val messages: Flow<List<ChatMessageModel>>

    suspend fun getChatMessageList(myNickname: String, otherNickname: String, chatRoomId: String? = null)

    suspend fun removeEventListenerFromMessages()
}