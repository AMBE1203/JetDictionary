package com.example.jetdictionary.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LoginResponse(
    val access_token: String,
    val username: String,
    @PrimaryKey val id: String,
    val fullname: String,
    val avatar: String?
): Parcelable