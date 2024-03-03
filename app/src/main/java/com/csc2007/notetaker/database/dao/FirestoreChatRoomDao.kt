package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import com.csc2007.notetaker.database.ChatRoom
import kotlinx.coroutines.flow.Flow

//@Dao
//interface FirestoreChatRoomDao {
//
//    fun getRooms(): List<ChatRoom> {
//        val docRef = db.collection("cities").document("SF")
//        docRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, "Current data: ${snapshot.data}")
//            } else {
//                Log.d(TAG, "Current data: null")
//            }
//        }
//    }
//
//}