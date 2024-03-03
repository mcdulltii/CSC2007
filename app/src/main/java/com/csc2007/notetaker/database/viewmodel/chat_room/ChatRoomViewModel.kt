package com.csc2007.notetaker.database.viewmodel.chat_room

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.csc2007.notetaker.database.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore


class ChatRoomViewModel(private val firestore_db: FirebaseFirestore, private val username: String, private val email: String): ViewModel()
{
    // Get all the rooms that this person currently is in
    private val ChatRoomCollRef = "Rooms"
    /* TODO: CRUD functions using the observer methods */
    fun getAllRooms(messages_in_room: MutableState<List<ChatRoom>>){
        /* TODO implement the logic for this using firestore_db*/
    }
}