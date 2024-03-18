package com.csc2007.notetaker.database

import java.net.URI
import java.sql.Timestamp

data class ChatRoom(
    val last_message_content: String?,
    val last_sender_user: String?,
    val room_name: String?,
    val last_sent_message_time: Timestamp?,
    val room_profile_picture: URI?, // TODO: not sure, will check again
    val roomId: String?,
    val user_list: List<String>?
)