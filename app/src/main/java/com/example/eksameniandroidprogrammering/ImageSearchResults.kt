package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import kotlinx.android.synthetic.main.image_search_results.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageSearchResults : AppCompatActivity(), OnItemCLickListener {

    private val dataList: MutableList<ImagesApi> = mutableListOf()
    val bingUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/bing"
    val googleUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/google"
    val tineyeUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/tineye"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_search_results)


        //Coroutine del
        GlobalScope.launch (Dispatchers.Default){
            getData()
            getImage()
        }
    }

    //Coroutine del
    private suspend fun getData():String? {
        var data: String? = null
        withContext(Dispatchers.IO){
            data = bingUrl
        }
        return data
    }

   private fun getImage(){
        val response=intent.getStringExtra("response")

        println("sent response$response")

        val adapter = Adapter(dataList, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
        recycler_view.adapter = adapter

        AndroidNetworking.initialize(this)

        AndroidNetworking.get(bingUrl)
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
                    if (anError != null) {
                        Toast.makeText(this@ImageSearchResults, anError.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    println("ERROR: $anError.message")
                }
            })
    }

    override fun onItemClicked(position: Int) {
        //Toast.makeText(this, "You clicked on item # ${position + 1}", Toast.LENGTH_LONG).show()
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra("image_link", dataList[position].image_link)
        startActivity(intent)

    }
}