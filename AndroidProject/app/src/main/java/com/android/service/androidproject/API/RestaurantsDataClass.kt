package com.android.service.androidproject.API

import com.google.gson.annotations.SerializedName

class RestaurantsDataClass {
    @SerializedName("name")
    lateinit var name: String
    @SerializedName("image_url")
    lateinit var url: String
    @SerializedName("lat")
    lateinit var lat: String
    @SerializedName("lng")
    lateinit var lng: String
    @SerializedName("city")
    lateinit var city: String
    @SerializedName("phone")
    lateinit var phone: String
    @SerializedName("price")
    lateinit var price: String
    @SerializedName("address")
    lateinit var address: String
    @SerializedName("id")
    lateinit var id: String
}