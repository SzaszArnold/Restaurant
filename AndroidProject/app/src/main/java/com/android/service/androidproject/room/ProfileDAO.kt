package com.android.service.androidproject.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDAO {

    @Query("SELECT * FROM profile")
    fun getAll(): LiveData<List<Profile>>

    @Update
    suspend fun update(profile: Profile)

    @Query("UPDATE profile set favorite=:favorites where uid = :id")
    fun updateFavorites(id: Int, favorites: String)

    @Query("SELECT favorite from profile where uid = :id")
    fun getFavorites(id: Int): String
    @Query("SELECT favRes from profile where uid = :id")
    fun getFavoriteRestaurants(id: Int): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Query("DELETE FROM profile")
    suspend fun deleteAll()
}