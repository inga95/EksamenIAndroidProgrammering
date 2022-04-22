package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import kotlinx.android.synthetic.main.image_search_results.*

class ImageSearchResults : AppCompatActivity() {


    private val dataList: MutableList<ImagesApi> = mutableListOf()
    private lateinit var adapter: Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_search_results)

        val response=intent.getStringExtra("response")

        println("sent response" + response)
        adapter = Adapter(dataList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recycler_view.adapter = adapter


        AndroidNetworking.initialize(this)

        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing")
            .addQueryParameter(
                "url",
                response
            )
            .build()
            .getAsObject(Liste::class.java, object : ParsedRequestListener<Liste> {
                override fun onResponse(response: Liste) {
                    dataList.addAll(response)
                    adapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                }
            })
    }
}