package com.android.service.androidproject.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.android.service.androidproject.room.AppDatabase
import com.android.service.androidproject.room.Profile
import com.android.service.androidproject.room.ProfileRepository

import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProfileRepository
    val allProfile: LiveData<List<Profile>>
    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text

    //-----------
    init{
        val profileDAO=AppDatabase.getDatabase(application).profileDAO()
        repository=ProfileRepository(profileDAO)
        allProfile=repository.allProfile
    }
    fun insert(profile: Profile) = viewModelScope.launch {
        repository.insert(profile)
    }
    fun update(profile: Profile) = viewModelScope.launch {
        repository.update(profile)
    }
}

