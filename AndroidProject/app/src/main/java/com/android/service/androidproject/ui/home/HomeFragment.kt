package com.android.service.androidproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.ResponseDataClass
import com.android.service.androidproject.API.herokuAPI
import com.android.service.androidproject.R
import com.android.service.androidproject.recycle.CustomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        recyclerView = root.findViewById(R.id.recycler_view)
        herokuAPI.endpoints.getRestaurants("IL").enqueue(object : Callback<ResponseDataClass> {
            override fun onResponse(
                call: Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                if (response.isSuccessful) {

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = CustomAdapter(response.body()!!.restaurants)
                    recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Toast.makeText(context, "Failed-onFailure", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }
}