package com.android.service.androidproject.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.service.androidproject.R
import com.android.service.androidproject.room.Profile


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.allProfile.observe(viewLifecycleOwner, Observer { profile ->
            // Update the cached copy of the words in the adapter.
            Log.d("tesztek","$profile")
        })

        return root
    }

}