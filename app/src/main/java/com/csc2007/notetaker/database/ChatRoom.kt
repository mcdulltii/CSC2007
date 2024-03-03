package com.csc2007.notetaker.database

data class ChatRoom(
    val roomId: Int,
    val memberList: List<String>
)