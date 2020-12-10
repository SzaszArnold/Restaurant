package com.android.service.androidproject.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val BASE_URL_API = "https://ratpark-api.imok.space/"//"http://opentable.herokuapp.com/api/"

interface herokuAPI {
    @GET("stats")
    fun getStats(): Call<JSONArray>
    @GET("countries")
    fun getCountries(): Call<JSONArray>
    @GET("cities")
    fun getCities(): Call<CityDataClass>
    @GET("restaurants/{id}")
    fun getRestaurantsByID(@Path("id")id: Int): Call<RestaurantsDataClass>
    @GET("restaurants")
    fun getRestaurantsByName(@Query("name")name: String): Call<ResponseDataClass>
    @GET("restaurants")
    fun getRestaurantsByCity(@Query("city")city: String): Call<ResponseDataClass>
    @GET("restaurants")
    fun getRestaurantsByPrice(@Query("country")state: String, @Query("per_page")per_page: Int, @Query("price")current_page: Int): Call<ResponseDataClass>
    @GET("restaurants")
    fun getRestaurants(@Query("country")state: String, @Query("per_page")per_page: Int, @Query("page")current_page: Int): Call<ResponseDataClass>
    companion object {
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val okHttp = OkHttpClient.Builder().addInterceptor(logger)

        val endpoints = create(BASE_URL_API)

        fun create(url: String): herokuAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build())
                .build()

            return retrofit.create(herokuAPI::class.java)
        }
    }
}