package com.android.service.androidproject.ui.home

import android.os.Bundle
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
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import com.android.service.androidproject.recycle.PaginationScrollListener
import com.android.service.androidproject.view.HomeViewModel
import com.android.service.androidproject.view.RestaurantsViewModel


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var restaurantsViewModel: RestaurantsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var editSearch: EditText
    private lateinit var btnSearch: Button
    private var filter = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        activity?.let {
            restaurantsViewModel = ViewModelProvider(it).get(RestaurantsViewModel::class.java)
        }
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        spinnerFilter = root.findViewById(R.id.spinnerFilter)
        spinnerCity = root.findViewById(R.id.spinnerCity)
        editSearch = root.findViewById(R.id.editSearch)
        btnSearch = root.findViewById(R.id.button)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        //observe the restaurants data in the view model, and adding to the recycleView
        restaurantsViewModel.apisRestaurants.observe(viewLifecycleOwner, Observer { profile ->
            recyclerView.adapter = CustomAdapter(profile)
        })
        //Pagination
        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return restaurantsViewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return restaurantsViewModel.isLoading
            }

            override fun loadMoreItems() {
                if (filter == "None") {
                    restaurantsViewModel.isLoading = true
                    loadMore()
                }
            }
        })

        return root
    }

    //loading more data to the recycleView
    private fun loadMore() {
        restaurantsViewModel.loadMore()
        restaurantsViewModel.page++
        restaurantsViewModel.isLoading = false
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
                view: View?, position: Int, id: Long
            ) {
                if (type[position] == "None") {
                    //set filter elements
                    filter = "None"
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.GONE
                    restaurantsViewModel.loadFirst()
                }
                if (type[position] == "Fav") {
                    //set filter elements
                    filter = ""
                    btnSearch.visibility = View.VISIBLE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.GONE
                    val list = arrayListOf<RestaurantsDataClass>()
                    //adding favorized data from the Room Database to the recycleView
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
                                restaurantsDataClass.id = e.id
                                restaurantsDataClass.phone = e.phone
                                restaurantsDataClass.price = e.price
                                list.add(restaurantsDataClass)
                            }
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
                    //set filter elements
                    filter = ""
                    spinnerCity.visibility = View.GONE
                    btnSearch.visibility = View.VISIBLE
                    editSearch.visibility = View.VISIBLE
                    if (type[position] == "Price") {
                        btnSearch.setOnClickListener {
                            restaurantsViewModel.loadPrice(editSearch.text.toString().toInt())
                        }

                    }
                    if (type[position] == "Name") {
                        btnSearch.setOnClickListener {
                            restaurantsViewModel.loadName(editSearch.text.toString())
                        }
                    }
                }
                if (type[position] == "City") {
                    //set filter elements
                    filter = ""
                    btnSearch.visibility = View.GONE
                    editSearch.visibility = View.GONE
                    spinnerCity.visibility = View.VISIBLE
                    restaurantsViewModel.loadCity()
                    restaurantsViewModel.apisCities.observe(
                        viewLifecycleOwner,
                        Observer { city ->
                            val cType = city.city
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
                                    view: View?, position: Int, id: Long
                                ) {
                                    restaurantsViewModel.loadByCity(cType[position])
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                }
                            }
                        })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

}