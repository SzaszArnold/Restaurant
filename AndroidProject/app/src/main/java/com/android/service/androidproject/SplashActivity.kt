package com.android.service.androidproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.recycle.CustomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var context = this@SplashActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        herokuAPI.endpoints.getRestaurants("IL").enqueue(object : Callback<ResponseDataClass> {
            override fun onResponse(
                call: Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                if (response.isSuccessful) {
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Toast.makeText(context, "Failed-onFailure", Toast.LENGTH_SHORT).show()
            }
        })
        // applying delay just to show some app logo or stuff (not good)

    }
}