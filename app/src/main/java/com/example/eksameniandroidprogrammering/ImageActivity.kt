package com.example.eksameniandroidprogrammering

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ImageActivity: AppCompatActivity() {

    //targeting sub-requirement #5

    var utils: Utils = Utils()
    private var dbHandler  = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.images_activity)

        val ivBig: ImageView = findViewById(R.id.iv_big)
        val bundle: Bundle? = intent.extras

        val imageLink = bundle?.getString("image_link")


        //Using Picasso to load images
        Picasso.get()
            .load(imageLink)
            .into(ivBig)

        val saveInAppBtn = findViewById<Button>(R.id.save_image)

        saveInAppBtn.setOnClickListener {
                Toast.makeText(applicationContext, "Database is down", Toast.LENGTH_LONG).show()
                //saveImage()
        }
    }

    override fun onStart() {
        super.onStart()
        println("Activity 2 onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Activity 2 onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Activity 2 onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Activity 2 onStop")
    }

    override fun onRestart() {
        super.onRestart()
        println("Activity 2 onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Activity 2 onDestroy")
    }


    //targeting sub-requirement #8
    fun saveImage(data: Intent?) {
        println("it clicks")

            val result = data!!.getStringExtra("RESULT")
            println("this is result" + result)
            var resultUri: Uri? = null
            if (result != null) {
                resultUri = Uri.parse(result)
            }
            var iv_pick_image: ImageView? = null
            iv_pick_image = findViewById(R.id.iv_big)
            iv_pick_image!!.setImageURI(resultUri)
            println(resultUri)

            val bitmap = utils.UriToBitmap(this, 101, resultUri.toString())
            println("you got one step " + bitmap)

            val byteArray = utils.bitmapToByteArray(bitmap)
            println("you got two steps " + byteArray)

            dbHandler.writableDatabase.insert("Images", null, ContentValues().apply {
                put("image", byteArray)
            })
            println("you got to the end")
        }
    }

