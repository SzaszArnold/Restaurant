package com.android.service.androidproject.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RestaurantsDAO {

    @Query("SELECT * FROM restaurants")
    fun getAll(): LiveData<List<Restaurants>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurants: Restaurants)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAll()
}