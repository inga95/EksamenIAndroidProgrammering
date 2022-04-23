package com.example.eksameniandroidprogrammering

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.images_activity.*

class ImageActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.images_activity)

        val ivBig : ImageView = findViewById(R.id.iv_big)
        val bundle : Bundle? = intent.extras

        val imageLink = bundle?.getString("image_link")

        Picasso.get()
            .load(imageLink)
            .into(ivBig)
    }
}
