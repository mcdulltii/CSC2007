package com.csc2007.notetaker.database.viewmodel.chat_room

import android.util.Log
import androidx.compose.runtime.MutableState
import com.csc2007.notetaker.database.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp

class ChatMessageViewModel(private val firestore_db: FirebaseFirestore, private val RoomId: String = "")
{
    private val ChatMessageCollRef: String = "ChatMessages"

    // READ TODO make sure the room ID matches the query for all the chatters
    fun getMessagesFromRoom(messages_in_room: MutableState<List<ChatMessage>>){
        val messages = mutableListOf<ChatMessage>()
        val docRef = firestore_db.collection(ChatMessageCollRef).orderBy("time_stamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("observer", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    var sender_user: String? = null
                    var sender_email: String? = null
                    var content: String? = null
                    var time_stamp: Timestamp? = null
                    var id: String?

                    id = doc.id
                    doc.getString("sender_user")?.let {
                        sender_user = it
                    }
                    doc.getString("sender_email")?.let{
                        sender_email = it
                    }
                    doc.getString("content")?.let{
                        content = it
                    }

                    /* TODO Figure out how to parse the time_stamp back to Timestamp object in Kotlin zzzz */
//                    doc.getString("time_stamp")?.let { timeStampString ->
//                        // Parse timeStampString to Date
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//                        val date = dateFormat.parse(timeStampString)
//
//                        // Convert Date to Timestamp
//                        time_stamp = date?.let { Timestamp(it.time) }
//                    }
                    val newMessage = ChatMessage(message_id = id, sender_user = sender_user, sender_email = sender_email, content = content, time_stamp = time_stamp, image = null)

                    // Add the new message to the list if it's not already present
                    if (!messages.contains(newMessage)) {
                        messages.add(newMessage)
                    }
                }

                // Update messages state with only the new messages
                messages_in_room.value = messages.toList()
                Log.d("observer", "Currently there are ${messages.size} many messages")

            }
    }

    fun insert(message: ChatMessage)
    {
        // TODO: initialize(?) a room specific model to store the latest message for this room specific
        firestore_db
            .collection(ChatMessageCollRef)
            .document()
            .set(message)
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