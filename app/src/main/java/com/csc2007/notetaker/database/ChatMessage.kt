package com.csc2007.notetaker.database

import com.google.firebase.firestore.Blob
import java.net.URI
import java.sql.Timestamp

data class ChatMessage(
    val message_id: String?,
    val sender_user: String?,
    val sender_email: String?,
    val time_stamp: Timestamp?,
    val content: String?,
    val pdf_link: URI?
)