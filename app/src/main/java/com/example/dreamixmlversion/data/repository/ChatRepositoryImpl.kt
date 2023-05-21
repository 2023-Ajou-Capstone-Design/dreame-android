package com.example.dreamixmlversion.data.repository

import android.util.Log
import com.example.dreamixmlversion.data.api.response.model.chat.ChatMessageModel
import com.example.dreamixmlversion.data.api.response.model.chat.ChatRoomItem
import com.example.dreamixmlversion.data.api.response.model.chat.ProcessedChatRoomItem
import com.example.dreamixmlversion.extension.convertBase64ToStr
import com.example.dreamixmlversion.extension.convertStrToBase64
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val database = Firebase.database.reference

    private val _chatRooms = MutableStateFlow<List<ProcessedChatRoomItem>>(emptyList())
    override val chatRooms: Flow<List<ProcessedChatRoomItem>>
        get() = _chatRooms.asStateFlow()

    override suspend fun getChatRoomList(userId: String) {
        database.child(USER_INFO).child(userId.convertStrToBase64()).child(CHATROOM_INFO)
            .orderByChild(
                LAST_UPLOAD_TIME
            ).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chatRoomItemList = mutableListOf<ProcessedChatRoomItem>()
                    for (snapshot in dataSnapshot.children) {
                        val chatRoomItem =
                            snapshot.getValue(ChatRoomItem::class.java) as ChatRoomItem
                        val processedChatRoomItem = chatRoomItem.copy(
                            otherUserId = chatRoomItem.otherUserId!!.convertBase64ToStr()
                        )
                        chatRoomItemList.add(
                            processedChatRoomItem.toProcessCustomChatRoomItem(
                                snapshot.key.toString()
                            )
                        )
                    }
                    _chatRooms.value = chatRoomItemList
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private var chatRoomId: String? = null

    override suspend fun addMessage(
        otherUserId: String?,
        isExistChatRoom: Boolean,
        myUserId: String,
        chatRoomId: String,
        messageContent: String?
    ) {
        if (!isExistChatRoom) {
            // "/ChatRoomMessageInfo"에 push()로 채팅방 ID 생성
            val chatRoomKey = database.child(CHATROOM_MESSAGE_INFO).push().key.toString()
            this.chatRoomId = chatRoomKey

            val setChatRoom = hashMapOf<String, Any>(
                // "/UserInfo/$UserId/ChatOpponentInfo/$OtherUserId"에 ChatRoomId write
                "/$USER_INFO/${myUserId.convertStrToBase64()}/$OPPONENT_INFO/${
                    otherUserId!!.convertStrToBase64()
                }" to chatRoomKey,
                // "/UserInfo/$OtherUserId/ChatOpponentInfo/$UserId"에 ChatRoomId write
                "/$USER_INFO/${otherUserId.convertStrToBase64()}/$OPPONENT_INFO/${
                    myUserId.convertStrToBase64()
                }" to chatRoomKey
            )

            val u = myUserId.convertStrToBase64()
            database.updateChildren(setChatRoom)
        }

        if (chatRoomId.isNotEmpty()) {
            this.chatRoomId = chatRoomId
        }

        val chatRoomUpdates = hashMapOf(
            // "/UserInfo/$UserId/ChatOpponentInfo/$OtherUserId"에 chatRoomItem write
            "$USER_INFO/${myUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/otherUserId" to
                    otherUserId!!.convertStrToBase64(),
            "$USER_INFO/${myUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageWriterUserId" to myUserId.convertStrToBase64(),
            "$USER_INFO/${myUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageContent" to messageContent!!,
            "$USER_INFO/${myUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastUploadTime" to ServerValue.TIMESTAMP,
            "$USER_INFO/${myUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/unreadMessageNumber" to 0,

            // "/UserInfo/$OtherUserId/ChatOpponentInfo/$UserId"에 chatRoomItem write
            "$USER_INFO/${otherUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/otherUserId" to
                    myUserId.convertStrToBase64(),
            "$USER_INFO/${otherUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageWriterUserId" to myUserId.convertStrToBase64(),
            "$USER_INFO/${otherUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageContent" to messageContent,
            "$USER_INFO/${otherUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastUploadTime" to ServerValue.TIMESTAMP,
            "$USER_INFO/${otherUserId.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/unreadMessageNumber" to 0,
        )

        database.updateChildren(chatRoomUpdates)


        // todo "/ChatRoomMessageInfo"에 MessageModel write
        val messageModel = ChatMessageModel(
            writerUserId = myUserId.convertStrToBase64(),
            messageContent = messageContent,
            uploadTime = ServerValue.TIMESTAMP
        )

        database.child(CHATROOM_MESSAGE_INFO).child(this.chatRoomId.toString()).child(MESSAGE_INFO)
            .push().setValue(messageModel)
    }

    private val _messages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    override val messages: Flow<List<ChatMessageModel>>
        get() = _messages.asStateFlow()

    override suspend fun getChatMessageList(
        myUserId: String,
        otherUserId: String,
        chatRoomId: String?
    ) {
        _messages.value = emptyList()
        if (!chatRoomId.isNullOrBlank()) {
            this.chatRoomId = chatRoomId
        }

        if (this.chatRoomId == null) {
            return
        } else {
            connectMessageToRTDB(this.chatRoomId!!)
        }
    }

    private val messageChildEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // todo 2. 받아온 메시지 목록을 Flow에 넣는다.
            val chatMessage =
                snapshot.getValue(ChatMessageModel::class.java) as ChatMessageModel
            val processedChatMessage =
                chatMessage.copy(writerUserId = chatMessage.writerUserId!!.convertBase64ToStr())
            _messages.value = _messages.value.toList() + processedChatMessage
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    private fun connectMessageToRTDB(chatRoomId: String) {
        // todo 1. "/ChatRoomMessageInfo/$ChatRoomId"에 있는 메시지 목록을 받아온다.
        database.child(CHATROOM_MESSAGE_INFO).child(chatRoomId).child(MESSAGE_INFO)
            .addChildEventListener(messageChildEventListener)
    }

    override suspend fun removeEventListenerFromMessages(chatRoomId: String) {
        database.child(CHATROOM_MESSAGE_INFO).child(chatRoomId).child(MESSAGE_INFO)
            .removeEventListener(messageChildEventListener)
    }

    companion object {
        const val USER_INFO = "ChatUserInfo"
        const val OPPONENT_INFO = "ChatOpponentInfo"
        const val CHATROOM_INFO = "ChatRoomInfo"
        const val CHATROOM_MESSAGE_INFO = "ChatRoomMessageInfo"
        const val LAST_UPLOAD_TIME = "LastUploadTime"
        const val MESSAGE_INFO = "MessageInfo"
    }
}