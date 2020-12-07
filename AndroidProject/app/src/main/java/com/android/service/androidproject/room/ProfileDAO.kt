package com.android.service.androidproject.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDAO {

    @Query("SELECT * FROM profile")
    fun getAll(): LiveData<List<Profile>>
@Update
suspend fun update(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Query("DELETE FROM profile")
    suspend fun deleteAll()
}