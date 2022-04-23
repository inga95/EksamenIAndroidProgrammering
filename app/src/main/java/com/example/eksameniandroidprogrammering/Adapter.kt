package com.example.eksameniandroidprogrammering

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view.view.*
import android.widget.Toast




class Adapter (private val dataList: MutableList<ImagesApi>): RecyclerView.Adapter<ViewHolder>() {


    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(v)

        //context = parent.context
       // return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        val imageView = holder.itemView.image_view

        Picasso.get()
            .load(data.image_link)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}