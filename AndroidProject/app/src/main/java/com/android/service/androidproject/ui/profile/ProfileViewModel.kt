package com.android.service.androidproject.ui.profile

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
    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    //-----------
    init {
        val profileDAO = AppDatabase.getDatabase(application).profileDAO()
        repository = ProfileRepository(profileDAO)
        allProfile = repository.allProfile
    }

    fun insert(profile: Profile) = viewModelScope.launch {
        repository.insert(profile)
    }

    fun updateFavoriteRestaurants(id: Int, favorites: String) = viewModelScope.launch {
        repository.updateFavorites(id, favorites)
    }

    fun getFavorites(id: Int) = viewModelScope.launch {
        repository.getFavorites(id)
    }

    fun update(profile: Profile) = viewModelScope.launch {
        repository.update(profile)
    }
}

