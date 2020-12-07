package com.android.service.androidproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val adr: String,
    @ColumnInfo(name = "phoneNr") val phoneNr: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "favorite") val favorites: String,
    @ColumnInfo(name = "img") val img: String


) {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
}