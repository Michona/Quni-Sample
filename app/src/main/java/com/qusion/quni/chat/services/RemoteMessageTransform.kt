package com.qusion.quni.chat.services

import com.google.firebase.messaging.RemoteMessage
import com.qusion.quni.chat.services.data.Notification
import timber.log.Timber

internal object RemoteMessageTransform {


    /**
     * @param remote is the [RemoteMessage] we receive from FCM
     *
     * It transfers the [RemoteMessage] into [Notification] and executes a function block
     * on that [Notification].
     *
     * This is the chunk of the logic where we decide what kind of [Notification] we want to show
     * and which data to use from [RemoteMessage].
     *
     * Its in a separate object [RemoteMessageTransform] since its much easier to test than in the Service.
     * */
    fun toLocalNotification(remote: RemoteMessage): Notification {

        if (remote.data.isEmpty()) {
            Timber.e("Notification data empty!")
            return Notification.Error()
        }

        return Notification.RoomMessage(
            content = remote.data["content"].toString(),
            userName = remote.data["userName"].toString(),
            roomName = remote.data["roomName"].toString(),
            userIcon = remote.data["userIcon"].toString()
        )
    }
}