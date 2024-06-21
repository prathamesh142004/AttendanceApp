package com.example.newattendanceapp

import android.os.Parcel
import android.os.Parcelable

data class SignInEntry(
    val date: String,
    val signInTime: String,
    var signOutTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(signInTime)
        parcel.writeString(signOutTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SignInEntry> {
        override fun createFromParcel(parcel: Parcel): SignInEntry {
            return SignInEntry(parcel)
        }

        override fun newArray(size: Int): Array<SignInEntry?> {
            return arrayOfNulls(size)
        }
    }
}
