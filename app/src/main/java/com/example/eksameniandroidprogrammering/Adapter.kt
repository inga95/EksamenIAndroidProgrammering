package com.example.eksameniandroidprogrammering

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view.view.*


class Adapter(private val dataList: MutableList<ImagesApi>, private val onItemCLickListener: ImageSearchResults): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        val imageView = holder.itemView.image_view

        Picasso.get()
            .load(data.image_link)
            .into(imageView)

        holder.itemView.setOnClickListener {
            onItemCLickListener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}