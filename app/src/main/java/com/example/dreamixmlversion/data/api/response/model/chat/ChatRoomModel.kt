package com.example.dreamixmlversion.data.api.response.model.chat

import com.google.firebase.database.ServerValue

data class ChatRoomItem(
    val otherUserId: String? = null,
    val lastMessageWriterUserId: String? = null,
    val lastMessageContent: String? = null,
    val lastUploadTime: Any? = null,
    val unreadMessageNumber: Int = 0
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "OtherUserId" to otherUserId,
            "WriterUserId" to lastMessageWriterUserId,
            "LastMessageContent" to lastMessageContent,
            "LastUploadTime" to lastUploadTime,
            "UnreadMessageNumber" to unreadMessageNumber
        )
    }

    fun toProcessCustomChatRoomItem(chatRoomId: String): ProcessedChatRoomItem {
        return ProcessedChatRoomItem(
            chatRoomId = chatRoomId,
            otherNickname = otherUserId,
            lastMessageWriterUserId = lastMessageWriterUserId,
            lastMessageContent = lastMessageContent,
            lastUploadTime = lastUploadTime,
            unreadMessageNumber = unreadMessageNumber
        )
    }
}

data class ProcessedChatRoomItem(
    val chatRoomId: String? = null,
    val otherNickname: String? = null,
    val lastMessageWriterUserId: String? = null,
    val lastMessageContent: String? = null,
    val lastUploadTime: Any? = null,
    val unreadMessageNumber: Int = 0
)
