package com.android.service.androidproject.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.android.service.androidproject.R
import com.android.service.androidproject.view.RestaurantsViewModel

class SplashFragment : Fragment() {
    private lateinit var restaurantsViewModel: RestaurantsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_splash, container, false)
        activity?.let {
            restaurantsViewModel = ViewModelProvider(it).get(RestaurantsViewModel::class.java)
        }
        restaurantsViewModel.loadFirst()
        restaurantsViewModel.apisRestaurants.observe(viewLifecycleOwner, Observer { res ->
            //navigate to the home fragment if the data is loaded
            if (res.isNotEmpty()) {
                view?.findNavController()?.navigate(R.id.action_splashFragment_to_navigation_home)
            }

        })
        return root
    }


}
