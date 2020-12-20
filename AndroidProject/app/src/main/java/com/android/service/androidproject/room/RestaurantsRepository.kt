package com.android.service.androidproject.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class RestaurantsRepository(private val restaurantsDAO: RestaurantsDAO) {

    val allRestaurants: LiveData<List<Restaurants>> = restaurantsDAO.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(restaurants: Restaurants) {
        restaurantsDAO.insert(restaurants)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete() {
        restaurantsDAO.deleteAll()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: Int) {
        restaurantsDAO.deleteById(id)
    }

}