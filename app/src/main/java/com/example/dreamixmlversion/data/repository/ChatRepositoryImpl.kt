package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.FirebaseKey.CHATROOM_INFO
import com.example.dreamixmlversion.data.api.FirebaseKey.CHATROOM_MEMBER
import com.example.dreamixmlversion.data.api.FirebaseKey.CHATROOM_MESSAGE_INFO
import com.example.dreamixmlversion.data.api.FirebaseKey.FCM_SERVER_KEY
import com.example.dreamixmlversion.data.api.FirebaseKey.FCM_TOKEN
import com.example.dreamixmlversion.data.api.FirebaseKey.LAST_UPLOAD_TIME
import com.example.dreamixmlversion.data.api.FirebaseKey.MESSAGE_INFO
import com.example.dreamixmlversion.data.api.FirebaseKey.OPPONENT_INFO
import com.example.dreamixmlversion.data.api.FirebaseKey.USER_INFO
import com.example.dreamixmlversion.data.api.response.model.chat.ChatMessageModel
import com.example.dreamixmlversion.data.api.response.model.chat.ChatRoomItem
import com.example.dreamixmlversion.data.api.response.model.chat.ProcessedChatRoomItem
import com.example.dreamixmlversion.extension.convertBase64ToStr
import com.example.dreamixmlversion.extension.convertStrToBase64
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val database = Firebase.database.reference

    private val _chatRooms = MutableStateFlow<List<ProcessedChatRoomItem>>(emptyList())
    override val chatRooms: Flow<List<ProcessedChatRoomItem>>
        get() = _chatRooms.asStateFlow()

    override suspend fun getChatRoomList(nickname: String) {
        database.child(USER_INFO).child(nickname.convertStrToBase64()).child(CHATROOM_INFO)
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
                    _chatRooms.value = chatRoomItemList.sortedByDescending {
                        it.lastUploadTime.toString().toLong()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private var chatRoomId: String? = null

    override suspend fun sendMessage(
        otherNickname: String?,
        isExistChatRoom: Boolean,
        myNickname: String,
        chatRoomId: String,
        messageContent: String
    ) {
        addMessage(otherNickname, isExistChatRoom, myNickname, chatRoomId, messageContent)
        sendNotification(myNickname, otherNickname!!, messageContent)
    }

    private fun addMessage(
        otherNickname: String?,
        isExistChatRoom: Boolean,
        myNickname: String,
        chatRoomId: String,
        messageContent: String
    ) {
        if (!isExistChatRoom) {
            createNewChatRoom(myNickname, otherNickname!!)
        }

        if (chatRoomId.isNotEmpty()) {
            this.chatRoomId = chatRoomId
        }

        createNewMessage(myNickname, messageContent)
        updateLastMessage(myNickname, otherNickname!!, messageContent)
    }

    private fun createNewChatRoom(myNickname: String, otherNickname: String) {
        // "/ChatRoomMessageInfo"에 push()로 채팅방 ID 생성
        val chatRoomKey = database.child(CHATROOM_MESSAGE_INFO).push().key.toString()
        this.chatRoomId = chatRoomKey

        val setChatRoom = hashMapOf<String, Any>(
            // "/UserInfo/$UserId/ChatOpponentInfo/$OtherUserId"에 ChatRoomId write
            "/$USER_INFO/${myNickname.convertStrToBase64()}/$OPPONENT_INFO/${
                otherNickname.convertStrToBase64()
            }" to chatRoomKey,
            // "/UserInfo/$OtherUserId/ChatOpponentInfo/$UserId"에 ChatRoomId write
            "/$USER_INFO/${otherNickname.convertStrToBase64()}/$OPPONENT_INFO/${
                myNickname.convertStrToBase64()
            }" to chatRoomKey
        )

        database.updateChildren(setChatRoom)

        registerChatMember(chatRoomKey, myNickname, otherNickname)
    }

    private fun registerChatMember(chatRoomId: String, myNickname: String, otherNickname: String) {
        database.child(CHATROOM_MESSAGE_INFO).child(chatRoomId).child(CHATROOM_MEMBER)
            .setValue(listOf(myNickname.convertStrToBase64(), otherNickname.convertStrToBase64()))
    }

    private fun updateLastMessage(myNickname: String, otherNickname: String, messageContent: String) {
        val chatRoomUpdates = hashMapOf(
            // "/UserInfo/$UserId/ChatOpponentInfo/$OtherUserId"에 chatRoomItem write
            "$USER_INFO/${myNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/otherUserId" to
                    otherNickname.convertStrToBase64(),
            "$USER_INFO/${myNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageWriterUserId" to myNickname.convertStrToBase64(),
            "$USER_INFO/${myNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageContent" to messageContent,
            "$USER_INFO/${myNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastUploadTime" to ServerValue.TIMESTAMP,
            "$USER_INFO/${myNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/unreadMessageNumber" to 0,

            // "/UserInfo/$OtherUserId/ChatOpponentInfo/$UserId"에 chatRoomItem write
            "$USER_INFO/${otherNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/otherUserId" to
                    myNickname.convertStrToBase64(),
            "$USER_INFO/${otherNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageWriterUserId" to myNickname.convertStrToBase64(),
            "$USER_INFO/${otherNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastMessageContent" to messageContent,
            "$USER_INFO/${otherNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/lastUploadTime" to ServerValue.TIMESTAMP,
            "$USER_INFO/${otherNickname.convertStrToBase64()}/$CHATROOM_INFO/${this.chatRoomId}/unreadMessageNumber" to 0,
        )

        database.updateChildren(chatRoomUpdates)
    }

    private fun createNewMessage(myNickname: String, messageContent: String) {
        // todo "/ChatRoomMessageInfo"에 MessageModel write
        val messageModel = ChatMessageModel(
            nickname = myNickname.convertStrToBase64(),
            messageContent = messageContent,
            uploadTime = ServerValue.TIMESTAMP
        )

        database.child(CHATROOM_MESSAGE_INFO).child(this.chatRoomId.toString()).child(MESSAGE_INFO)
            .push().setValue(messageModel)
    }

    private fun sendNotification(myNickname: String, otherNickname: String, messageContent: String) {

        database.child(USER_INFO).child(otherNickname.convertStrToBase64()).child(FCM_TOKEN)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val otherUserFcmToken = snapshot.value

                    val client = OkHttpClient()

                    val root = JSONObject()
                    val notification = JSONObject()
                    notification.put("title", myNickname)
                    notification.put("body", messageContent)

                    root.put("to", otherUserFcmToken)
                    root.put("priority", "high")
                    root.put("notification", notification)

                    val requestBody =
                        root.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaType())
                    val request =
                        Request.Builder().post(requestBody)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .header("Authorization", "key=$FCM_SERVER_KEY").build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.stackTraceToString()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            // ignore onResponse
                        }

                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    private val _messages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    override val messages: Flow<List<ChatMessageModel>>
        get() = _messages.asStateFlow()

    private lateinit var chatRoomMessageDatabaseRef: DatabaseReference

    override suspend fun getChatMessageList(
        myNickname: String,
        otherNickname: String,
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
                chatMessage.copy(nickname = chatMessage.nickname!!.convertBase64ToStr())
            _messages.value = _messages.value.toList() + processedChatMessage
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    private fun connectMessageToRTDB(chatRoomId: String) {
        // todo 1. "/ChatRoomMessageInfo/$ChatRoomId"에 있는 메시지 목록을 받아온다.
        chatRoomMessageDatabaseRef =
            database.child(CHATROOM_MESSAGE_INFO).child(chatRoomId).child(MESSAGE_INFO)
        chatRoomMessageDatabaseRef
            .addChildEventListener(messageChildEventListener)
    }

    override suspend fun removeEventListenerFromMessages() {
        chatRoomMessageDatabaseRef
            .removeEventListener(messageChildEventListener)
    }
}