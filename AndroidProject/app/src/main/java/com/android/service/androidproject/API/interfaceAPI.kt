package com.android.service.androidproject.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val BASE_URL_API = "http://opentable.herokuapp.com/api/"
interface herokuAPI {
    @GET("stats")
    fun getCountries(): Call<JSONArray>
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