package com.android.service.androidproject.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class ProfileRepository(private val profileDAO: ProfileDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allProfile: LiveData<List<Profile>> = profileDAO.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
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