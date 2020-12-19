package com.android.service.androidproject.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.service.androidproject.room.AppDatabase
import com.android.service.androidproject.room.Profile
import com.android.service.androidproject.room.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProfileRepository
    val allProfile: LiveData<List<Profile>>
    init {
        val profileDAO = AppDatabase.getDatabase(application).profileDAO()
        repository = ProfileRepository(profileDAO)
        allProfile = repository.allProfile
    }

    fun insert(profile: Profile) = viewModelScope.launch {
        repository.insert(profile)
    }
    fun update(profile: Profile) = viewModelScope.launch {
        repository.update(profile)
    }
    fun delete() = viewModelScope.launch {
        repository.delete()
    }
}

