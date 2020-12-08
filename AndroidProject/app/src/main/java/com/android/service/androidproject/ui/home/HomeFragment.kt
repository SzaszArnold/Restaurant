package com.android.service.androidproject.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.MainActivity
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import com.android.service.androidproject.recycle.PaginationScrollListener
import com.android.service.androidproject.ui.profile.ProfileViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var editSearch: EditText
    private lateinit var btnSearch: Button
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
        spinnerFilter = root.findViewById(R.id.spinnerFilter)
        spinnerCity = root.findViewById(R.id.spinnerCity)
        editSearch = root.findViewById(R.id.editSearch)
        btnSearch = root.findViewById(R.id.button)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CustomAdapter(getList())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        val list = getList()
        for (e in list) {
            Log.d("tesztek", "${e.name}")
        }

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

    override fun onResume() {
        super.onResume()
        val chartType = resources.getStringArray(R.array.Filter)
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, chartType) }
        spinnerFilter.adapter = adapter

        spinnerFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (chartType[position] == "None" || chartType[position] == "Fav") {
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.GONE
                }
                if (chartType[position] == "Name" || chartType[position] == "Price") {
                    spinnerCity.visibility = View.GONE
                    btnSearch.visibility = View.VISIBLE
                    editSearch.visibility = View.VISIBLE
                    btnSearch.setOnClickListener {
                        herokuAPI.endpoints.getRestaurantsByPrice("IL", 25,editSearch.text.toString().toInt())
                            .enqueue(object : Callback<ResponseDataClass> {
                                override fun onResponse(
                                    call: Call<ResponseDataClass>,
                                    response: Response<ResponseDataClass>
                                ) {
                                    if (response.isSuccessful) {
                                        recyclerView.adapter = CustomAdapter(response.body()!!.restaurants)

                                    }
                                }
                                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                                    Log.d("onFailure", "Here DetailFragment")
                                }
                            })
                    }


                }
                if (chartType[position] == "City") {
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.VISIBLE

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }

        }
    }
    fun getList(): ArrayList<RestaurantsDataClass> {
        val gson = Gson()
        val json = sharedPreferences1.getString("Restaurants", null)
        val type = object :
            TypeToken<ArrayList<RestaurantsDataClass>>() {}.type//converting the json to list
        return gson.fromJson(json, type)//returning the list
    }

}