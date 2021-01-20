package com.qusion.quni.chat.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import coil.request.Disposable
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.qusion.quni.R
import com.qusion.quni.chat.services.data.Channel
import com.qusion.quni.chat.services.data.Notification
import java.util.*

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService : FirebaseMessagingService() {

    private var coilDisposable: Disposable? = null

    override fun onMessageReceived(message: RemoteMessage) {

        /**/
        val notification: Notification = RemoteMessageTransform.toLocalNotification(message)

        /* Start loading image */
        if (notification is Notification.RoomMessage) {
            coilDisposable = loadBitmap(url = notification.userIcon) { profileBitmap ->

                with(NotificationManagerCompat.from(this)) {
                    notify(
                        NOTIFICATION_ID,
                        getNotificationBuilder(
                            notification = notification,
                            profileIcon = profileBitmap
                        ).build()
                    )
                }
            }
        }
    }

    override fun onCreate() {
        createNotificationChannels()
    }

    override fun onDestroy() {
        super.onDestroy()
        coilDisposable?.dispose()
    }

    /**
     * The main place for styling the notification
     * depends on the type of [Notification]
     * @see Notification
     * */
    private fun getNotificationBuilder(
        notification: Notification,
        profileIcon: Bitmap
    ): NotificationCompat.Builder {

        val notificationBuilder = NotificationCompat.Builder(this, notification.channel.id)
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        when (notification) {
            is Notification.RoomMessage -> {

                val senderPerson = Person.Builder().setName(notification.userName).setIcon(
                    IconCompat.createWithBitmap(profileIcon)
                ).build()

                val style = NotificationCompat.MessagingStyle("You")
                style.isGroupConversation = true
                style.conversationTitle = notification.roomName

                val currentStyle = recoverMessagingStyle()
                currentStyle?.messages?.forEach {
                    style.addMessage(it.text, it.timestamp, it.person)
                }

                style.addMessage(notification.content, Date().time, senderPerson)

                notificationBuilder.apply {
                    setStyle(style)
                }
            }
            is Notification.GeneralMessage -> TODO()
            is Notification.Error -> TODO()
        }

        return notificationBuilder
    }

    private fun recoverMessagingStyle(): NotificationCompat.MessagingStyle? {
        val activeNotification =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .activeNotifications.find { it.id == NOTIFICATION_ID }?.notification ?: return null

        return NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
            activeNotification
        )
    }

    /** @see Channel */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Channel.values().forEach {
                val channel = NotificationChannel(
                    it.id,
                    it.name,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = it.getDescription()
                }

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 169
    }
}
