package com.example.eksameniandroidprogrammering

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view.view.*

class Adapter (private val dataList: MutableList<ImagesApi>): RecyclerView.Adapter<Holder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
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