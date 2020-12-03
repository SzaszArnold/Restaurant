package com.android.service.androidproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(@PrimaryKey @ColumnInfo(name = "name") val name: String)