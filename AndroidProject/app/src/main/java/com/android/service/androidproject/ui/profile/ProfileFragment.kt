package com.android.service.androidproject.ui.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.android.service.androidproject.bitMapToString
import com.android.service.androidproject.room.Profile
import com.android.service.androidproject.stringToBitMap
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profName: TextView
    private lateinit var profAddress: TextView
    private lateinit var profPhone: TextView
    private lateinit var profImg: ImageView
    private lateinit var profEmail: TextView
    private lateinit var btnModify: Button


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        profName = root.findViewById(R.id.profName)
        profAddress = root.findViewById(R.id.profAddress)
        profEmail = root.findViewById(R.id.profEmail)
        profPhone = root.findViewById(R.id.profPhone)
        profImg = root.findViewById(R.id.profImg)
        btnModify = root.findViewById(R.id.btnModify)
        val list =listOf("147604","149062")
        val gson = Gson()
        val json = gson.toJson(list)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
      /* profileViewModel.insert(
            Profile(
                "Arni",
                "Romania",
                "0749091739",
                "arnoldszasz06@gmail.com",
                json,
                ""
            )
       )*/
        profileViewModel.allProfile.observe(viewLifecycleOwner, Observer { profile ->
            val index = profile.lastIndex
            Log.d("testpro", "${profile[index]}, $index")
            profName.text = "Name: " + profile[index].name
            profAddress.text = "Address: " + profile[index].adr
            profEmail.text = "E-mail: " + profile[index].email
            profPhone.text = "Phone: " + profile[index].phoneNr
            Glide.with(profImg)
                .load(profile[index].img)
                .override(500, 500)
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(profImg)
        })

        btnModify.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_navigation_profile_to_profileUpdate)
        }
        return root
    }



}

