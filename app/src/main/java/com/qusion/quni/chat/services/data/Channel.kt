package com.qusion.quni.chat.services.data

enum class Channel(val id: String) {
    GENERAL_INFO("general_info_channel"),
    ROOM_CHAT("room_chat_channel");

    /** This could also return StringRes. as Int */
    fun getDescription(): String {
        return when (this) {
            ROOM_CHAT -> "Notification channel used for room chat."
            GENERAL_INFO -> "Notification channel for general info"
        }
    }
}
