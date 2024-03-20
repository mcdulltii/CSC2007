package com.csc2007.notetaker.database.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class ChatRoomFileCollection(private val firestorage: FirebaseStorage, roomObserver: ChatRoomViewModel, username: String, time_stamp: Timestamp) {
    val storageRef = firestorage.reference
    val sharedFilesRef: StorageReference = storageRef.child("sharedfiles")
    val room = roomObserver
    val timeStamp = time_stamp
    val user = username
    @WorkerThread
    fun addFile(roomId: String, fileName: String, fileByteArr: ByteArray): String {
        val roomSharedFilesRef =
            sharedFilesRef.child("${roomId}/${System.currentTimeMillis()}/${fileName}")
        var downloadUrl = ""

        fileByteArr.let { fileByte ->
            roomSharedFilesRef.putBytes(fileByte)
                .addOnSuccessListener {
                    roomSharedFilesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        downloadUrl = downloadUri.toString()
                        room.uploadPDF(uri = downloadUrl, pdfName = fileName, room_id = roomId)
                        Log.d("pdf", "$downloadUrl")
                    }
                }
                .addOnFailureListener { err ->
                    Log.d("Firebase Storage", "Error occurred: ${err.message}")
                }
        }
        room.updateAfterShareNotes("shared their notes with the group.", time_stamp = timeStamp, user = user, roomId = roomId)
        return downloadUrl
    }

    @WorkerThread
    suspend fun readFile(fileUrl: String): ByteArray {
        return withContext(Dispatchers.IO) {
            try {
                val fileRef = firestorage.getReferenceFromUrl(fileUrl)
                val bytes = fileRef.getBytes(Long.MAX_VALUE).await()
                bytes
            } catch (e: Exception) {
                // Handle exceptions
                ByteArray(0)
            }
        }
    }

    @WorkerThread
    fun deleteFile(fileUrl: String) {
        val fileRef = firestorage.getReferenceFromUrl(fileUrl)

        fileRef.delete()
            .addOnSuccessListener {
                Log.d("Firebase Storage", "File successfully deleted from storage")
            }
            .addOnFailureListener { err ->
                Log.d("Firebase Storage", "Error occurred: ${err.message}")
            }
    }
}