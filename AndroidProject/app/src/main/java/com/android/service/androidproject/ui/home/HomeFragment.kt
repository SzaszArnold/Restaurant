package com.android.service.androidproject.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val list = getList()
        for (e in list) {
            Log.d("tesztek", "${e.name}")
        }
        recyclerView=root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CustomAdapter(getList())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )


        return root
    }

    fun getList(): ArrayList<RestaurantsDataClass> {
        val gson = Gson()
        val sharedPreferences1 =
            requireContext().getSharedPreferences("Restaurants", Context.MODE_PRIVATE)
        val json = sharedPreferences1.getString("Restaurants", null)
        val type = object :
            TypeToken<ArrayList<RestaurantsDataClass>>() {}.type//converting the json to list
        return gson.fromJson(json, type)//returning the list
    }
}