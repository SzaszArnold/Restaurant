package com.android.service.androidproject.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.service.androidproject.API.CityDataClass
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantsViewModel : ViewModel() {
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    var page = 1
    private val mutableLiveData = MutableLiveData<SplashState>()
    val apisRestaurants: MutableLiveData<List<RestaurantsDataClass>> = MutableLiveData()
    val apisCities: MutableLiveData<CityDataClass> = MutableLiveData()
    fun loadFirst() {
        herokuAPI.endpoints.getRestaurants("US", 25, 1)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisRestaurants.postValue(response.body()!!.restaurants)
                        mutableLiveData.postValue(SplashState.MainActivity())
                        Log.d("testelek", "${apisRestaurants.value}")
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {

                }
            })
    }

    fun loadMore() {
        herokuAPI.endpoints.getRestaurants("US", 25, page)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisRestaurants.postValue(apisRestaurants.value!!.plus(response.body()!!.restaurants))
                        if (response.body()!!.restaurants.size != 25) {
                            isLastPage = true
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {

                }
            })
    }

    fun loadPrice(price: Int) {
        herokuAPI.endpoints.getRestaurantsByPrice("US", 25, price)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisRestaurants.postValue(response.body()!!.restaurants)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseDataClass>,
                    t: Throwable
                ) {
                    Log.d("onFailure", "Here DetailFragment")
                }
            })
    }

    fun loadName(name: String) {
        herokuAPI.endpoints.getRestaurantsByName(name)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisRestaurants.postValue(response.body()!!.restaurants)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseDataClass>,
                    t: Throwable
                ) {
                    Log.d("onFailure", "Here DetailFragment")
                }
            })
    }

    fun loadCity() {
        herokuAPI.endpoints.getCities()
            .enqueue(object : Callback<CityDataClass> {
                override fun onResponse(
                    call: Call<CityDataClass>,
                    response: Response<CityDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisCities.postValue(response.body()!!)
                    }
                }

                override fun onFailure(
                    call: Call<CityDataClass>,
                    t: Throwable
                ) {
                    Log.d("onFailure", "Here DetailFragment")
                }
            })
    }

    fun loadByCity(city: String) {
        herokuAPI.endpoints.getRestaurantsByCity(city)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        apisRestaurants.postValue(response.body()!!.restaurants)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseDataClass>,
                    t: Throwable
                ) {
                    Log.d("onFailure", "Here DetailFragment")
                }
            })
    }
}

sealed class SplashState {
    class MainActivity : SplashState()
}