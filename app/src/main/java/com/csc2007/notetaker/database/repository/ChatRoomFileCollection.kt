package com.csc2007.notetaker.database.repository

import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatRoomFileCollection(private val firestorage: FirebaseStorage) {
    val storageRef = firestorage.reference
    val sharedFilesRef: StorageReference = storageRef.child("sharedfiles")

    @WorkerThread
    fun addFile(messageId: String, fileName: String, fileURI: Uri): String {
        val roomSharedFilesRef = sharedFilesRef.child("${messageId}/${fileName}")
        var downloadUrl = ""

        fileURI.let { uri ->
            roomSharedFilesRef.putFile(uri)
                .addOnSuccessListener {
                    roomSharedFilesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        downloadUrl = downloadUri.toString()
                    }
                }
                .addOnFailureListener { err ->
                    Log.d("Firebase Storage", "Error occurred: ${err.message}")
                }
        }

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