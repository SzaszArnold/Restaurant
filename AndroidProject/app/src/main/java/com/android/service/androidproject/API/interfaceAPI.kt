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

val BASE_URL_API = "http://opentable.herokuapp.com/api/"
interface herokuAPI {
    @GET("stats")
    fun getStats(): Call<JSONArray>
    @GET("countries")
    fun getCountries(): Call<JSONArray>
    @GET("cities")
    fun getCities(): Call<JSONArray>
    @GET("restaurants/{page}")
    fun getRestaurants(@Path("page")page:Int): Call<JSONObject>
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