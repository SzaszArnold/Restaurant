package com.android.service.androidproject.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.android.service.androidproject.R
import com.android.service.androidproject.view.ProfileViewModel
import com.bumptech.glide.Glide

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
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.allProfile.observe(viewLifecycleOwner, Observer { profile ->
            //if no data, will set default
            if (profile.isEmpty()) {
                profName.text = "Name: "
                profAddress.text = "Address: "
                profEmail.text = "E-mail: "
                profPhone.text = "Phone: "
            } else {
                //if the profile is not empty, putting data to the boxes
                val index = profile.lastIndex
                profName.text = "Name: " + profile[index].name
                profAddress.text = "Address: " + profile[index].adr
                profEmail.text = "E-mail: " + profile[index].email
                profPhone.text = "Phone: " + profile[index].phoneNr
                Glide.with(profImg)
                    .load(profile[index].img)
                    .override(500, 500)
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(profImg)
            }
        })
        //navigate to the profile update fragment
        btnModify.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_navigation_profile_to_profileUpdate)
        }
        return root
    }


}

