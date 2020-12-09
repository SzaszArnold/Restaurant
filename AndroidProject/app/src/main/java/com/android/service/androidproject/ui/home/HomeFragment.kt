package com.android.service.androidproject.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.CityDataClass
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import com.android.service.androidproject.recycle.PaginationScrollListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
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
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        sharedPreferences1 =
            requireContext().getSharedPreferences("Restaurants", Context.MODE_PRIVATE)
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
        val type = resources.getStringArray(R.array.Filter)
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, type) }
        spinnerFilter.adapter = adapter

        spinnerFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                if (type[position] == "None") {
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.GONE
                }
                if (type[position] == "Fav") {
                    btnSearch.visibility = View.VISIBLE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.GONE
                    val list = arrayListOf<RestaurantsDataClass>()
                    homeViewModel.allRestaurants.observe(
                        viewLifecycleOwner,
                        Observer { restaurants ->
                            for (e in restaurants) {
                                val restaurantsDataClass = RestaurantsDataClass()
                                restaurantsDataClass.address = e.adr
                                restaurantsDataClass.name = e.name
                                restaurantsDataClass.url = e.img_url
                                restaurantsDataClass.lng = e.lng
                                restaurantsDataClass.lat = e.lat
                                restaurantsDataClass.city = e.city
                                restaurantsDataClass.id=e.id
                                restaurantsDataClass.phone=e.phone
                                restaurantsDataClass.price=e.price
                                list.add(restaurantsDataClass)
                            }
                        Log.d("testpro","$list")


                        })
                    btnSearch.setOnClickListener {

                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = CustomAdapter(list)
                        recyclerView.addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                }
                if (type[position] == "Name" || type[position] == "Price") {
                    spinnerCity.visibility = View.GONE
                    btnSearch.visibility = View.VISIBLE
                    editSearch.visibility = View.VISIBLE
                    if (type[position] == "Price") {
                        btnSearch.setOnClickListener {
                            herokuAPI.endpoints.getRestaurantsByPrice(
                                "IL",
                                25,
                                editSearch.text.toString().toInt()
                            )
                                .enqueue(object : Callback<ResponseDataClass> {
                                    override fun onResponse(
                                        call: Call<ResponseDataClass>,
                                        response: Response<ResponseDataClass>
                                    ) {
                                        if (response.isSuccessful) {
                                            recyclerView.layoutManager = LinearLayoutManager(context)
                                            recyclerView.adapter =
                                                CustomAdapter(response.body()!!.restaurants)
                                            recyclerView.addItemDecoration(
                                                DividerItemDecoration(
                                                    context,
                                                    DividerItemDecoration.VERTICAL
                                                )
                                            )


                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ResponseDataClass>,
                                        t: Throwable
                                    ) {
                                        Log.d("onFailure", "Here DetailFragment")
                                    }
                                })
                        }

                    }
                    if (type[position] == "Name") {
                        btnSearch.setOnClickListener {
                            herokuAPI.endpoints.getRestaurantsByName(
                                editSearch.text.toString()
                            )
                                .enqueue(object : Callback<ResponseDataClass> {
                                    override fun onResponse(
                                        call: Call<ResponseDataClass>,
                                        response: Response<ResponseDataClass>
                                    ) {
                                        if (response.isSuccessful) {
                                            recyclerView.layoutManager = LinearLayoutManager(context)
                                            recyclerView.adapter =
                                                CustomAdapter(response.body()!!.restaurants)
                                            recyclerView.addItemDecoration(
                                                DividerItemDecoration(
                                                    context,
                                                    DividerItemDecoration.VERTICAL
                                                )
                                            )

                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ResponseDataClass>,
                                        t: Throwable
                                    ) {
                                        Log.d("onFailure", "Here DetailFragment")
                                    }
                                })
                        }

                    }
                }
                if (type[position] == "City") {
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.VISIBLE

                    herokuAPI.endpoints.getCities()
                        .enqueue(object : Callback<CityDataClass> {
                            override fun onResponse(
                                call: Call<CityDataClass>,
                                response: Response<CityDataClass>
                            ) {
                                if (response.isSuccessful) {
                                    val cType = response.body()!!.city
                                    val adapter =
                                        context?.let {
                                            ArrayAdapter(
                                                it,
                                                android.R.layout.simple_spinner_item,
                                                cType
                                            )
                                        }
                                    spinnerCity.adapter = adapter
                                    spinnerCity.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View, position: Int, id: Long
                                        ) {
                                            herokuAPI.endpoints.getRestaurantsByCity(
                                                cType[position]
                                            )
                                                .enqueue(object : Callback<ResponseDataClass> {
                                                    override fun onResponse(
                                                        call: Call<ResponseDataClass>,
                                                        response: Response<ResponseDataClass>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            recyclerView.layoutManager = LinearLayoutManager(context)
                                                            recyclerView.adapter =
                                                                CustomAdapter(response.body()!!.restaurants)
                                                            recyclerView.addItemDecoration(
                                                                DividerItemDecoration(
                                                                    context,
                                                                    DividerItemDecoration.VERTICAL
                                                                )
                                                            )

                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<ResponseDataClass>,
                                                        t: Throwable
                                                    ) {
                                                        Log.d("onFailure", "Here DetailFragment")
                                                    }
                                                })
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }

                                    }

                                }
                            }

                            override fun onFailure(
                                call: Call<CityDataClass>,
                                t: Throwable
                            ) {
                                Log.d("onFailure", "Here DetailFragment")
                            }
                        })


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