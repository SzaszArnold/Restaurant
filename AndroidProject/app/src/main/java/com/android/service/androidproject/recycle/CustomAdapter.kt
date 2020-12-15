package com.android.service.androidproject.recycle

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.android.service.androidproject.room.AppDatabase
import com.android.service.androidproject.room.ProfileRepository
import com.bumptech.glide.Glide

var bundle = bundleOf()
class CustomAdapter(private val data: List<RestaurantsDataClass>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val rowItem: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return ViewHolder(rowItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.resName.text = "Name: " + data[position].name
        holder.resAddress.text = "Address: " + data[position].address
        holder.resPrice.text = "Price: " + data[position].price
        bundle= bundleOf("uid" to data[position].id, "name" to data[position].name, "address" to data[position].address,
        "price" to data[position].price, "phone" to data[position].phone, "city" to data[position].city, "lat" to data[position].lat,
            "lng" to data[position].lng, "url" to data[position].url)
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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val resName: TextView
        val resAddress: TextView
        val resPrice: TextView
        val resImg: ImageView
        override fun onClick(view: View) {
            view.findNavController().navigate(R.id.action_navigation_home_to_detailFragment,bundle)
        }
        init {
            view.setOnClickListener(this)
            resName = view.findViewById(R.id.rName)
            resAddress = view.findViewById(R.id.rAddress)
            resPrice = view.findViewById(R.id.rPrice)
            resImg = view.findViewById(R.id.image_view)
        }

    }
}