package com.csc2007.notetaker.database.viewmodel.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.viewmodel.hashString
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ChatMessageViewModel(private val firestore_db_message_coll: FirebaseFirestore): ViewModel()
{

}