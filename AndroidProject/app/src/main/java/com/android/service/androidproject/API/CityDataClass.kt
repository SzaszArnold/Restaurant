package com.android.service.androidproject.API

import com.google.gson.annotations.SerializedName

class CityDataClass {
    @SerializedName("cities")
    lateinit var restaurants:List<String>

}