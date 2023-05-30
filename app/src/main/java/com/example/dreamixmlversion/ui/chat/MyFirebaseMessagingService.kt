package com.example.dreamixmlversion.ui.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import com.example.dreamixmlversion.MainViewModel
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.FirebaseKey
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.extension.convertStrToBase64
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MyFirebaseMessagingService() : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Create the NotificationChannel
        val name = "채팅 알림"
        val descriptionText = "채팅 알림입니다."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(
            getString(R.string.default_notification_channel_id),
            name,
            importance
        )
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val body = message.notification?.body
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.default_notification_channel_id)
        )
            .setSmallIcon(R.drawable.chat)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(body)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
}