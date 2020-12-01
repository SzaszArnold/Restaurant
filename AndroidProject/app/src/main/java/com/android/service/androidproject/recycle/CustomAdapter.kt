package com.android.service.androidproject.recycle

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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

        holder.foodName.text = "Name: "+data[position].name
        Glide.with(holder.itemView)
            .load(data[position].url)
            .centerCrop()
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(holder.img);
     //   holder.img.setImageBitmap(stringToBitMap(data[position].img))


    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val foodName: TextView
        val id: TextView
        val img: ImageView
        override fun onClick(view: View) {
            val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            view.context.startActivity(mapIntent)
        }

        init {
            view.setOnClickListener(this)
            foodName = view.findViewById(R.id.fName)
            id=view.findViewById(R.id.id)
            img = view.findViewById(R.id.image_view)

        }
    }
}