package com.android.service.androidproject.recycle

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.service.androidproject.API.RestaurantsDataClass
import com.android.service.androidproject.R
import com.bumptech.glide.Glide


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
        holder.foodName.text = "Name: " + data[position].name
        holder.id.text = "City: " + data[position].city
        holder.btn.hint = "geo:${data[position].lat},${data[position].lng}"
        holder.phone.text=data[position].phone
        Glide.with(holder.itemView)
            .load(data[position].url)
            .centerCrop()
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val foodName: TextView
        val id: TextView
        val img: ImageView
        val btn: Button
        val phone: TextView
        override fun onClick(view: View) {
        }

        init {
            view.setOnClickListener(this)
            foodName = view.findViewById(R.id.fName)
            id = view.findViewById(R.id.id)
            img = view.findViewById(R.id.image_view)
            btn = view.findViewById(R.id.btnMap)
            phone = view.findViewById(R.id.phone)
            phone.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "${phone.text}"))
                view.context.startActivity(intent)
            }
            btn.setOnClickListener {
                val gmmIntentUri = Uri.parse(btn.hint.toString())
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                view.context.startActivity(mapIntent)
            }
        }
    }

}