package com.android.service.androidproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.herokuAPI
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    private var context = this@SplashActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

       val sharedPreferences: SharedPreferences =
            getSharedPreferences("Restaurants", Context.MODE_PRIVATE)
        herokuAPI.endpoints.getRestaurants("IL",25).enqueue(object : Callback<ResponseDataClass> {
            override fun onResponse(
                call: Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                if (response.isSuccessful) {
                    try { val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(response.body()!!.restaurants)//converting list to Json
                        Log.d("json", json)
                        editor.putString("Restaurants",json)
                        editor.commit()
                        startActivity(Intent(context, MainActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                    }


                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Toast.makeText(context, "Failed-onFailure", Toast.LENGTH_SHORT).show()
            }
        })
    }

}