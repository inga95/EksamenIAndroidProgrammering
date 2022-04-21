package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var takePictureBtn: Button
    private lateinit var iv_pick_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.bt_search)

        searchBtn.setOnClickListener{
            val intent = Intent(this,ImageSearchResults::class.java)
            startActivity(intent)
        }




        //Gjør at man kan åpne kamera i emulatoren/mobilen og ta bilde
       /* takePictureBtn.setOnClickListener{
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 123)
        }*/

        var iv_pick_image: ImageView? = null
        var mGetContent: ActivityResultLauncher<String?>? = null
        iv_pick_image = findViewById(R.id.iv_pick_image)
        //iv_pick_image.bringToFront()
        iv_pick_image.setOnClickListener(View.OnClickListener {
            Log.d("== My activity ===", "OnClick is called")
            mGetContent!!.launch("image/*")
        })
        mGetContent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            val intent = Intent(this@MainActivity, Cropper::class.java)
            intent.putExtra("DATA", result.toString())
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1 && requestCode == 101) {
            val result = data!!.getStringExtra("RESULT")
            var resultUri: Uri? = null
            if (result != null) {
                resultUri = Uri.parse(result)
            }
            var iv_pick_image: ImageView? = null
            iv_pick_image = findViewById(R.id.iv_pick_image)
            iv_pick_image!!.setImageURI(resultUri)
        }else if (requestCode == 123){
            var pic = data?.getParcelableExtra<Bitmap>("data")
            iv_pick_image.setImageBitmap(pic)
        }
    }
}