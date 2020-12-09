package com.android.service.androidproject.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class ProfileRepository(private val profileDAO: ProfileDAO) {

    val allProfile: LiveData<List<Profile>> = profileDAO.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(profile: Profile) {
        profileDAO.insert(profile)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(profile: Profile) {
        profileDAO.insert(profile)
    }

}
