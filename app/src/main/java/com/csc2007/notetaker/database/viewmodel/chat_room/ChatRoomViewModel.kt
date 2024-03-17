package com.csc2007.notetaker.database.viewmodel.chat_room

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.ChatRoom
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import java.net.URI
import java.sql.Time
import java.sql.Timestamp


class ChatRoomViewModel(private val firestore_db: FirebaseFirestore, private val username: String = "", private val email: String = ""): ViewModel()
{
    // Get all the rooms that this person currently is in
    private val ChatRoomCollRef: String = "Rooms"
    private val user_list: String = "user_list";
    /* TODO: CRUD functions using the observer methods */
    fun getAllRooms(roomsUserIsIn: MutableState<List<ChatRoom>>, userEmail: String) {
        val rooms = mutableListOf<ChatRoom>()
        val roomIds = mutableSetOf<String>()

        val docRef = firestore_db.collection(ChatRoomCollRef)
            .whereArrayContains("user_list", userEmail) // Assuming "user_list" is the field name
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("observer", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (doc in value!!) {
                    val roomId = doc.id
                    roomIds.add(roomId)

                    val user_list = doc.get("user_list") as? List<String>
                    val last_message_content = doc.getString("last_message_content")
                    val last_sender_user = doc.getString("last_sender_user")
                    val room_name = doc.getString("room_name")
                    val time_stamp = doc.getTimestamp("last_sent_message_time")?.let {
                        val millisecondsSinceEpoch = it.seconds * 1000 + it.nanoseconds / 1000000
                        Timestamp(millisecondsSinceEpoch)
                    }
                    val image_link = doc.get("image_link") as? URI

                    val newRoom = ChatRoom(
                        roomId = roomId,
                        last_message_content = last_message_content,
                        last_sender_user = last_sender_user,
                        room_name = room_name,
                        last_sent_message_time = time_stamp,
                        user_list = user_list,
                        room_profile_picture = image_link
                    )

                    rooms.add(newRoom)
                }

                roomsUserIsIn.value = rooms.toList()

                // Clear the pipeline
                rooms.removeAll(rooms)

                Log.d("observer", "Currently there are ${rooms.size} many rooms")
            }
    }

    fun updateRoom(roomId: String, username: String, newName: String, usersToAdd: List<String>, time_stamp: Timestamp) {
        // get the current room's user list first then update it
        val docRef = firestore_db.collection(ChatRoomCollRef).document(roomId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user_list = document.get("user_list") as? List<String>
                    if (user_list != null) {
                        val updatedUserList = user_list.toMutableList()
                        updatedUserList.addAll(usersToAdd)

                        // update the document with the new user_list
                        docRef.update("user_list", updatedUserList)
                            .addOnSuccessListener {
                                // Document updated successfully
                                Log.d("Update Room", "Document successfully updated with new user_list")
                            }
                            .addOnFailureListener { e ->
                                // Error updating document
                                Log.w("Update Room", "Error updating document", e)
                            }
                    } else {
                        Log.e("Update Room", "user_list is null or not a List<String>")
                    }
                } else {
                    Log.e("Update Room", "Document $roomId does not exist or is null")
                }
            }

        // update the rest of the items
        firestore_db
            .collection(ChatRoomCollRef)
            .document(roomId)
            .set(
                hashMapOf(
                    "last_message_content" to "Made changes to the room",
                    "last_sent_message_time" to time_stamp,
                    "last_sender_user" to username,
//                    "user_list" to usersToAdd,
                    "room_name" to newName
                ),
                SetOptions.merge()
            )
            .addOnSuccessListener {
                Log.d("Sucessfully updated the room", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("Failed to update", "Error updating document", e)
            }
    }

    fun createRoom(room: ChatRoom) {
        firestore_db
            .collection(ChatRoomCollRef)
            .document()
            .set(room)
            .addOnSuccessListener {
                Log.d(
                    "Sucessfully insert",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "Failed to insert",
                    "Error writing document",
                    e
                )
            }
    }
}