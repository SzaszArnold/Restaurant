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
import com.android.service.androidproject.R
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profName: TextView
    private lateinit var profAddress: TextView
    private lateinit var profPhone: TextView
    private lateinit var profImg: ImageView
    private lateinit var profFavorites: TextView
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
        profFavorites = root.findViewById(R.id.profFavorite)
        profPhone = root.findViewById(R.id.profPhone)
        profImg = root.findViewById(R.id.profImg)
        btnModify = root.findViewById(R.id.btnModify)
        val list = listOf("1222", "5555", "99845")
        val gson = Gson()
        val json = gson.toJson(list)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        //  profileViewModel.insert(Profile("Alma","Nincs","Rou","0749091739","a@a.com",json))
        profileViewModel.allProfile.observe(viewLifecycleOwner, Observer { profile ->
            profName.text = "Name: " + profile[0].name
            profAddress.text = "Address: " + profile[0].adr
            profEmail.text="E-mail: "+profile[0].email
            profPhone.text="Phone: "+profile[0].phoneNr
            profFavorites.text="Fav: "+profile[0].favorite
            Glide.with(profImg)
                .load(R.drawable.ic_home_black_24dp)
                .centerCrop()
                .override(500,500)
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(profImg)


        })

        return root
    }

}

fun getList(json: String): List<String> {
    val gson = Gson()
    val type = object :
        TypeToken<List<String>>() {}.type//converting the json to list
    return gson.fromJson(json, type)//returning the list
}