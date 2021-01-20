package com.qusion.quni.chat.services.data

sealed class Notification(val channel: Channel) {
    data class RoomMessage(
        val content: String,
        val userName: String,
        val roomName: String,
        val userIcon: String,
        val roomIcon: String? = null
    ) : Notification(Channel.ROOM_CHAT)

    data class GeneralMessage(
        val title: String,
        val content: String
    ) : Notification(Channel.GENERAL_INFO)

    data class Error(val cause: Throwable? = null) : Notification(Channel.GENERAL_INFO)
}
