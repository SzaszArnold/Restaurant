package com.android.service.androidproject.ui.home

import android.app.Application
import android.content.ClipData
import android.view.View
import androidx.lifecycle.*
import com.android.service.androidproject.room.*
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