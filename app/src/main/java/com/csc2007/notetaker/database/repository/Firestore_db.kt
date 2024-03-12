package com.csc2007.notetaker.database.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore

class Firestore_db(){
    private val projectId = "notetaker-896ba"
    private val apiKey = "AIzaSyC0Z2FirGhnnIHIjXDGeTpaSXUMinpNBBc"
    private val applicationId = "com.csc2007.notetaker"
    fun get_firestore_db(context: Context): FirebaseFirestore{
        val firestoreOptions: FirebaseOptions = FirebaseOptions.Builder()
            .setProjectId(projectId)
            .setApiKey(apiKey)
            .setApplicationId(applicationId)
            .build()

        FirebaseApp.initializeApp(context, firestoreOptions, "firestore_db")

        val firestore_db = FirebaseFirestore.getInstance(FirebaseApp.getInstance("firestore_db"))
        return firestore_db
    }
}