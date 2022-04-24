package com.example.eksameniandroidprogrammering

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import kotlinx.coroutines.Dispatchers
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.Util
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var takePictureBtn: Button
    private lateinit var imageView: ImageView
    private var selectedImage: Uri? = null
    var utils: Utils = Utils()
    private val uploadUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/upload"

    var responseContainer = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.bt_search)
        val savedResultsBtn = findViewById<Button>(R.id.saved_result)

        searchBtn.setOnClickListener{
            val intent = Intent(this,ImageSearchResults::class.java)
            intent.putExtra("response", responseContainer)
            startActivity(intent)
        }

        GlobalScope.launch (Dispatchers.Default){
            downloadData()
        }

        //Gjør at man kan åpne kamera i emulatoren/mobilen og ta bilde
       /*takePictureBtn.setOnClickListener{
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

    private suspend fun downloadData():String? {
        var downloadedData: String? = null
        withContext(Dispatchers.IO){
            downloadedData = uploadUrl
        }
        return downloadedData
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
            println(resultUri)

            val bitmap = utils.UriToBitmap(this, 101, resultUri.toString())
            val file = utils.bitmapToFile(bitmap, "image.png", this)
            println(file)

                AndroidNetworking.upload(uploadUrl)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile(
                        "image", file
                    )
                    .setTag(this)
                    .build()
                    .setUploadProgressListener { bytesUploaded, totalBytes ->
                        val toast =
                            Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
                            responseContainer = response
                            println("RESPONSE: $response")
                        }

                        override fun onError(anError: ANError) {
                            Toast.makeText(this@MainActivity, anError.message, Toast.LENGTH_SHORT)
                                .show()
                            println("ERROR: $anError.message")
                        }
                    })
        }
        else if (requestCode == 123){
            var pic = data?.getParcelableExtra<Bitmap>("data")
            imageView.setImageBitmap(pic)
        }
    }

    private fun addRecord() {
        val image = iv_pick_image.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!image.isEmpty()) {
            val status =
                databaseHandler.addImages(DataModel(0, image))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Image cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }



}