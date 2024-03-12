package com.csc2007.notetaker.database.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.storage.FirebaseStorage

class Firestorage_db(){
    private val projectId = "csc2007-notetaker"
    private val apiKey = "AIzaSyB8MOkjvEo1GfN3QLigF7cc2cJ-YwE1mJM"
    private val applicationId = "com.csc2007.notetaker"
    private val bucketName = "csc2007-notetaker.appspot.com"
    fun get_firestorage_db(context: Context): FirebaseStorage {
        val storageOptions: FirebaseOptions = FirebaseOptions.Builder()
            .setProjectId(projectId)
            .setApiKey(apiKey)
            .setApplicationId(applicationId)
            .setStorageBucket(bucketName)
            // Add other necessary options for Firebase Storage project
            .build()

        FirebaseApp.initializeApp(context, storageOptions, "firestorage_db")

// Access Firebase Storage for the Firebase Storage project
        val firestorage = FirebaseStorage.getInstance(FirebaseApp.getInstance("firestorage_db"))
        return firestorage
    }
}