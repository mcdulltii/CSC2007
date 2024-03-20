package com.csc2007.notetaker.database

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class ChatRoom(
    val last_message_content: String?,
    val last_sender_user: String?,
    val room_name: String?,
    val last_sent_message_time: Timestamp?,
    val roomId: String?,
    val user_list: List<String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readSerializable() as? Timestamp,
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(last_message_content)
        parcel.writeString(last_sender_user)
        parcel.writeString(room_name)
        parcel.writeSerializable(last_sent_message_time)
        parcel.writeString(roomId)
        parcel.writeStringList(user_list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatRoom> {
        override fun createFromParcel(parcel: Parcel): ChatRoom {
            return ChatRoom(parcel)
        }

        override fun newArray(size: Int): Array<ChatRoom?> {
            return arrayOfNulls(size)
        }
    }
}
