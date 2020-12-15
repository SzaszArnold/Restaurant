package com.android.service.androidproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.android.service.androidproject.view.RestaurantsViewModel
import com.android.service.androidproject.view.SplashState

class SplashActivity : AppCompatActivity() {
    private var requestCode = 123
    private lateinit var restaurantsViewModel: RestaurantsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestAllPermissions()
        restaurantsViewModel = ViewModelProvider(this).get(RestaurantsViewModel::class.java)
            restaurantsViewModel.liveData.observe(this, Observer {
                when (it) {
                    is SplashState.MainActivity -> {
                        goToMainActivity()
                    }
                }
            })


    }

    private fun goToMainActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun requestAllPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission is not granted")
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.INTERNET),
                this.requestCode
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission is not granted")
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                this.requestCode
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission is not granted")
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                this.requestCode
            )
        }
    }

}
