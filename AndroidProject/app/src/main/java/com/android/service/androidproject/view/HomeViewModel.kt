package com.android.service.androidproject.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
    fun delete() = viewModelScope.launch {
        repository.delete()
    }

}