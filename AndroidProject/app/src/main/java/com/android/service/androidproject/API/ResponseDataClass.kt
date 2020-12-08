package com.android.service.androidproject.API

import com.google.gson.annotations.SerializedName

class ResponseDataClass {
    @SerializedName("restaurants")
    lateinit var restaurants:List<RestaurantsDataClass>

}