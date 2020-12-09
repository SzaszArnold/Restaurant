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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.R
import com.android.service.androidproject.room.Restaurants
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        homeViewModel.allRestaurants.observe(viewLifecycleOwner, Observer { restaurants ->
            val index = restaurants.lastIndex
            Log.d("testpro", "${restaurants[index]}, $index")

        })
        requireArguments().getString("uid")?.let {
            herokuAPI.endpoints.getRestaurantsByID(it.toInt())
                .enqueue(object : Callback<RestaurantsDataClass> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<RestaurantsDataClass>,
                        response: Response<RestaurantsDataClass>
                    ) {
                        if (response.isSuccessful) {
                            resName.text = "Name: " + response.body()!!.name
                            resAddress.text =
                                "Address: " + response.body()!!.city + ", " + response.body()!!.address
                            resPrice.text = "Price: " + response.body()!!.price
                            resPhone.text = "Nr: " + response.body()!!.phone
                            resPhone.hint = response.body()!!.phone
                            resMap.text = "Map"
                            resMap.hint = "geo:${response.body()!!.lat},${response.body()!!.lng}"
                            Glide.with(resImg)
                                .load(response.body()!!.url)
                                .centerCrop()
                                .override(500, 500)
                                .placeholder(R.drawable.ic_home_black_24dp)
                                .into(resImg)
                            resFavorites.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (imageUri != null) {
                                    val restaurant = Restaurants(
                                        response.body()!!.id,
                                        response.body()!!.name,
                                        response.body()!!.address,
                                        response.body()!!.city,
                                        response.body()!!.price,
                                        response.body()!!.phone,
                                        response.body()!!.lat,
                                        response.body()!!.lng,
                                        imageUri.toString()
                                    )
                                    homeViewModel.insert(restaurant)
                                }
                                else{
                                    val restaurant = Restaurants(
                                        response.body()!!.id,
                                        response.body()!!.name,
                                        response.body()!!.address,
                                        response.body()!!.city,
                                        response.body()!!.price,
                                        response.body()!!.phone,
                                        response.body()!!.lat,
                                        response.body()!!.lng,
                                        response.body()!!.url
                                    )
                                    homeViewModel.insert(restaurant)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<RestaurantsDataClass>, t: Throwable) {
                        Log.d("onFailure", "Here DetailFragment")
                    }
                })
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
            val gmmIntentUri = Uri.parse(resMap.hint.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            requireContext().startActivity(mapIntent)
        }

        return root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
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