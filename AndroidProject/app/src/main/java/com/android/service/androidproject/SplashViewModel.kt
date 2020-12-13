package com.android.service.androidproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel : ViewModel() {
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    var page=1
    private val mutableLiveData = MutableLiveData<SplashState>()
    val apisRestaurants: MutableLiveData<List<RestaurantsDataClass>> = MutableLiveData()
    init {
        GlobalScope.launch {
            herokuAPI.endpoints.getRestaurants("US", 25, 1)
                .enqueue(object : Callback<ResponseDataClass> {
                    override fun onResponse(
                        call: Call<ResponseDataClass>,
                        response: Response<ResponseDataClass>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Testelek", "1x")
                            apisRestaurants.postValue(response.body()!!.restaurants)
                            mutableLiveData.postValue(SplashState.MainActivity())
                        }
                    }

                    override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {

                    }
                })
        }
    }
    fun <RestaurantsDataClass> conc(vararg list: List<RestaurantsDataClass>): List<RestaurantsDataClass>{
        return listOf(*list).flatten()
    }
    fun loadMore(){
        herokuAPI.endpoints.getRestaurants("US", 25, page)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Testelek", "1x page: $page")
                        apisRestaurants.postValue(conc(response.body()!!.restaurants))
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {

                }
            })
    }
    fun page(){
        page++
    }
}
sealed class SplashState {
    class MainActivity : SplashState()
}