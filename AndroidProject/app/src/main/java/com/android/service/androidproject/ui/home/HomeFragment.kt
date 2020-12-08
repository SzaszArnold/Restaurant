package com.android.service.androidproject.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import com.android.service.androidproject.recycle.PaginationScrollListener
import com.android.service.androidproject.ui.profile.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var recyclerView: RecyclerView
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page: Int = 2
    lateinit var sharedPreferences1: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences1 =
            requireContext().getSharedPreferences("Restaurants", Context.MODE_PRIVATE)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val list = getList()
        for (e in list) {
            Log.d("tesztek", "${e.name}")
        }
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CustomAdapter(getList())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                Log.d("Belepett", "ide, page: $page")
                getMoreItems()
                isLoading = false
                recyclerView.adapter = CustomAdapter(getList())

            }
        })




        return root
    }

    private fun getMoreItems() {
        herokuAPI.endpoints.getRestaurants("IL", 25, page)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val editor = sharedPreferences1.edit()
                            val list = getList()
                            list.addAll(response.body()!!.restaurants)
                            Log.d("sharedbol", "$list")
                            val gson = Gson()
                            val json = gson.toJson(list)//converting list to Json
                            Log.d("json", json)
                            editor.putString("Restaurants", json)
                            editor.commit()
                            page++
                        } catch (e: Exception) {
                        }

                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                    Toast.makeText(context, "Failed-onFailure", Toast.LENGTH_SHORT).show()
                }
            })

    }

    fun getList(): ArrayList<RestaurantsDataClass> {
        val gson = Gson()
        val json = sharedPreferences1.getString("Restaurants", null)
        val type = object :
            TypeToken<ArrayList<RestaurantsDataClass>>() {}.type//converting the json to list
        return gson.fromJson(json, type)//returning the list
    }

}