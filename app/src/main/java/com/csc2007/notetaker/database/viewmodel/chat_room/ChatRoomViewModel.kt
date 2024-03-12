package com.csc2007.notetaker.database.viewmodel.chat_room

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.net.URI
import java.sql.Timestamp


class ChatRoomViewModel(private val firestore_db: FirebaseFirestore, private val username: String, private val email: String): ViewModel()
{
    // Get all the rooms that this person currently is in
    private val ChatRoomCollRef: String = "Rooms"
    private val user_list: String = "user_list";
    /* TODO: CRUD functions using the observer methods */
    fun getAllRooms(roomsUserIsIn: MutableState<List<ChatRoom>>, userEmail: String){
        /* TODO implement the logic for this using firestore_db */
        val rooms = mutableListOf<ChatRoom>()
        val docRef = firestore_db.collection(ChatRoomCollRef)/*.whereArrayContains(user_list, userEmail)*/
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("observer", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (doc in value!!) {
                    var last_message_content: String? = null
                    var last_sender_user: String? = null
                    var room_name: String? = null
                    var time_stamp: Timestamp? = null
                    var user_list: List<String>? = null
                    var image_link: URI? = null
                    var id: String?

                    id = doc.id
                    user_list = doc.get("user_list") as? List<String>
                    time_stamp = doc.get("last_sent_message_time") as? Timestamp
                    doc.getString("last_message_content")?.let {
                        last_message_content = it
                    }
                    doc.getString("last_sender_user")?.let{
                        last_sender_user = it
                    }
                    doc.getString("room_name")?.let{
                        room_name = it
                    }
//                    doc.getString("user_list")?.let{
//                        user_list = it as List<String>
//                    }

                    /* TODO Figure out how to parse the time_stamp back to Timestamp object in Kotlin zzzz */
//                    doc.getString("time_stamp")?.let { timeStampString ->
//                        // Parse timeStampString to Date
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//                        val date = dateFormat.parse(timeStampString)
//
//                        // Convert Date to Timestamp
//                        time_stamp = date?.let { Timestamp(it.time) }
//                    }
                    val newRoom = ChatRoom(roomId = id, last_message_content = last_message_content, last_sender_user = last_sender_user, room_name = room_name, last_sent_message_time = time_stamp, memberList = user_list, room_profile_picture = image_link)

                    // Add the new message to the list if it's not already present
                    if (!rooms.contains(newRoom)) {
                        rooms.add(newRoom)
                    }
                    roomsUserIsIn.value = rooms.toList()
                    Log.d("observer", "Currently there are ${rooms.size} many messages")
                }
            }
    }
}