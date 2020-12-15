package com.android.service.androidproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurants(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val adr: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lng") val lng: String,
    @ColumnInfo(name = "img_url") val img_url: String,
    @ColumnInfo(name="favorite") val fav: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
}



