package com.android.service.androidproject.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.room.AppDatabase
import com.android.service.androidproject.room.Restaurants
import com.android.service.androidproject.room.RestaurantsRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RestaurantsRepository
    val allRestaurants: LiveData<List<Restaurants>>
    init {
        val restaurantsDAO = AppDatabase.getDatabase(application).restaurantDAO()
        repository = RestaurantsRepository(restaurantsDAO)
        allRestaurants = repository.allRestaurants
    }

    fun insert(restaurants: Restaurants) = viewModelScope.launch {
        repository.insert(restaurants)
    }

}