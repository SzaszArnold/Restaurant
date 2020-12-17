package com.android.service.androidproject.recycle

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_view.view.*


class CustomAdapter(private val data: List<RestaurantsDataClass>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val rowItem: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return ViewHolder(rowItem, data)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.resName.text = "Name: " + data[position].name
        holder.resAddress.text = "Address: " + data[position].city + ", " + data[position].address
        holder.resPrice.text = "Price: " + data[position].price


        Glide.with(holder.itemView)
            .load(data[position].url)
            .centerCrop()
            .override(500, 500)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(holder.resImg)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View, restaurantsDataClass: List<RestaurantsDataClass>) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val resName: TextView
        val resAddress: TextView
        val resPrice: TextView
        val resImg: ImageView
        var restaurants: List<RestaurantsDataClass>
        override fun onClick(view: View) {
            var bundle = bundleOf()
            for (e in restaurants) {
                if ("Name: " + e.name == view.rName.text.toString()) {
                    bundle = bundleOf(
                        "uid" to e.id,
                        "name" to e.name,
                        "address" to e.address,
                        "price" to e.price,
                        "phone" to e.phone,
                        "city" to e.city,
                        "lat" to e.lat,
                        "lng" to e.lng,
                        "url" to e.url
                    )
                }
            }
            view.findNavController().navigate(R.id.action_navigation_home_to_detailFragment, bundle)
        }

        init {
            view.setOnClickListener(this)
            restaurants = restaurantsDataClass
            resName = view.findViewById(R.id.rName)
            resAddress = view.findViewById(R.id.rAddress)
            resPrice = view.findViewById(R.id.rPrice)
            resImg = view.findViewById(R.id.image_view)
        }

    }
}