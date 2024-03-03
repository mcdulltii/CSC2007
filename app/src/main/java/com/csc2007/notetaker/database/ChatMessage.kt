package com.csc2007.notetaker.database

import com.google.firebase.firestore.Blob
import java.sql.Timestamp

data class ChatMessage(
    val sender_user: String?,
    val sender_email: String?,
    val time_stamp: Timestamp?,
    val content: String?,
    val room_id_belong: Int?,
    val image: Blob?
)