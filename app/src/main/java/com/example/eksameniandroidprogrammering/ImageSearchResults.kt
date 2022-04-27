package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.eksameniandroidprogrammering.*
import kotlinx.android.synthetic.main.image_search_results.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageSearchResults : AppCompatActivity(), OnItemCLickListener {

    //targeting sub-requirement #5

    private val dataList: MutableList<ImagesApi> = mutableListOf()
    private val bingUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing"
    private val googleUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google"
    private val tineyeUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_search_results)

        Toast.makeText(applicationContext, "Results loading...", Toast.LENGTH_LONG).show()
        Toast.makeText(applicationContext, "Results loading...", Toast.LENGTH_LONG).show()


        //targeting sub-requirement #7
        GlobalScope.launch (Dispatchers.Default){
            try {
                getData()
                getImage()
            } catch (e : Exception){
                Toast.makeText(applicationContext, "There are no results for that picture", Toast.LENGTH_LONG).show()
            }
        }
    }

    //targeting sub-requirement #7
    private suspend fun getData():String? {
        var data: String? = null

        withContext(Dispatchers.IO){

            //The googleUrl can crash the app, try to start the app again, or remove the googleUrl
            data = googleUrl
            data = tineyeUrl
            data = bingUrl
        }
        return data
    }

   private suspend fun getImage(){
        val response=intent.getStringExtra("response")

        println("sent response$response")

        val adapter = Adapter(dataList, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recycler_view.adapter = adapter

        AndroidNetworking.initialize(this)

        AndroidNetworking.get(getData())
            .addQueryParameter(
                "url",
                response
            )
            .build()
            .getAsObject(Liste::class.java, object : ParsedRequestListener<Liste> {
                override fun onResponse(response: Liste) {
                    if (response.toString().isEmpty()) {
                        println("No content $response")
                        Toast.makeText(this@ImageSearchResults, "No content in body here", Toast.LENGTH_LONG).show()
                    } else {
                        dataList.addAll(response)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onError(anError: ANError?) {
                    if (anError != null) {
                        Toast.makeText(this@ImageSearchResults, anError.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    println("ERROR: $anError.message")
                }
            })
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra("image_link", dataList[position].image_link)
        startActivity(intent)
    }
}