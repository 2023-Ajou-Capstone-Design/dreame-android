package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.model.chat.ChatMessageModel
import com.example.dreamixmlversion.data.api.response.model.chat.ProcessedChatRoomItem
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val chatRooms: Flow<List<ProcessedChatRoomItem>>

    suspend fun getChatRoomList(userId: String)

    suspend fun addMessage(
        otherUserId: String? = null,
        isExistChatRoom: Boolean,
        myUserId: String,
        chatRoomId: String,
        messageContent: String? = null
    )

    val messages: Flow<List<ChatMessageModel>>

    suspend fun getChatMessageList(myUserId: String, otherUserId: String, chatRoomId: String? = null)

    suspend fun removeEventListenerFromMessages(chatRoomId: String)
}