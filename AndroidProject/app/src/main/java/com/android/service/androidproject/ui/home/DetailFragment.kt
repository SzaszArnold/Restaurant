package com.android.service.androidproject.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.service.androidproject.R
import com.android.service.androidproject.room.Restaurants
import com.android.service.androidproject.view.HomeViewModel
import com.bumptech.glide.Glide


class DetailFragment : Fragment() {
    private lateinit var resName: TextView
    private lateinit var resAddress: TextView
    private lateinit var resPrice: TextView
    private lateinit var resImg: ImageView
    private lateinit var resFavorites: CheckBox
    private lateinit var resMap: TextView
    private lateinit var resPhone: TextView
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var btnChange: Button
    private var imageUri: Uri? = null
    private var isFavorite = false
    private var mapCoordinate=""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_detail, container, false)
        resName = root.findViewById(R.id.rName)
        resAddress = root.findViewById(R.id.rAddress)
        resPrice = root.findViewById(R.id.rPrice)
        resImg = root.findViewById(R.id.image_view)
        resFavorites = root.findViewById(R.id.rCheckBox)
        resMap = root.findViewById(R.id.rMap)
        resPhone = root.findViewById(R.id.rPhone)
        btnChange = root.findViewById(R.id.changeBtn)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.allRestaurants.observe(viewLifecycleOwner, Observer { profile ->
            if (profile.isNotEmpty()) {
                if (profile[profile.lastIndex].id == requireArguments().getString("uid")) {
                    resFavorites.isChecked = true
                    isFavorite = true
                }
            }
        })
        resName.text = "Name: " + requireArguments().getString("name")
        resAddress.text =
            "Address: " + requireArguments().getString("city") + ", " + requireArguments().getString(
                "address"
            )
        resPrice.text = "Price: " + requireArguments().getString("price")
        resPhone.text = "Nr: " + requireArguments().getString("phone")
        resPhone.hint = requireArguments().getString("phone")
        mapCoordinate =
            "geo:${requireArguments().getString("lat")},${requireArguments().getString("lng")}"
        imageUri = requireArguments().getString("url")!!.toUri()
        Glide.with(resImg)
            .load(imageUri)
            .centerCrop()
            .override(500, 500)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(resImg)

        resFavorites.setOnClickListener {
            if (!isFavorite) {
                resFavorites.isChecked = true
                homeViewModel.insert(
                    Restaurants(
                        requireArguments().getString("uid").toString(),
                        requireArguments().getString("name").toString(),
                        requireArguments().getString("address").toString(),
                        requireArguments().getString("city").toString(),
                        requireArguments().getString("price").toString(),
                        requireArguments().getString("phone").toString(),
                        requireArguments().getString("lat").toString(),
                        requireArguments().getString("lng").toString(),
                        imageUri.toString(),
                        true
                    )
                )
            }
            if(isFavorite){
                homeViewModel.deleteById(requireArguments().getString("uid")!!.toInt())
            }
        }
        btnChange.setOnClickListener {
            pickImageFromGallery()
        }

        resPhone.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "${resPhone.hint}"))
            requireContext().startActivity(intent)
        }
        resMap.setOnClickListener {
            val gmmIntentUri = Uri.parse(mapCoordinate)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            requireContext().startActivity(mapIntent)
        }

        return root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            Glide.with(resImg)
                .load(imageUri)
                .centerCrop()
                .override(500, 500)
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(resImg)
        }
    }
}