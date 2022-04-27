package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.eksameniandroidprogrammering.*
import kotlinx.coroutines.Dispatchers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var dbHandler  = DatabaseHandler(this)
    var utils: Utils = Utils()
    private val uploadUrl: String = "http://api-edu.gtl.ai/api/v1/imagesearch/upload"

    var responseContainer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.bt_search)
        val savedResultsBtn = findViewById<Button>(R.id.saved_result)
        val checkImageView = findViewById<View>(R.id.iv_pick_image)

        val checkString = "https"

        searchBtn.setOnClickListener {
            if (responseContainer.contains(checkString)) {
                println("it contains the string")
                val intent = Intent(this, ImageSearchResults::class.java)
                intent.putExtra("response", responseContainer)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "You need to choose a picture", Toast.LENGTH_LONG).show()
            }
        }

        GlobalScope.launch (Dispatchers.Default){
            downloadData()
        }

        var iv_pick_image: ImageView? = null
        var mGetContent: ActivityResultLauncher<String?>? = null
        iv_pick_image = findViewById(R.id.iv_pick_image)

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

        if (resultCode == -1 && requestCode == 101 ) {
            val result = data!!.getStringExtra("RESULT")
            println("this is result" + result)
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

            Toast.makeText(applicationContext, "Image uploading...", Toast.LENGTH_SHORT).show()

                AndroidNetworking.upload(uploadUrl)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile(
                        "image", file
                    )
                    .setTag(this)
                    .build()
                    .setUploadProgressListener { bytesUploaded, totalBytes ->
                    }
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            responseContainer = response
                            println("RESPONSE: $response")
                            println(responseContainer)
                            Toast.makeText(applicationContext, "Upload successful" ,Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(anError: ANError) {
                            Toast.makeText(this@MainActivity, anError.message, Toast.LENGTH_SHORT)
                                .show()
                            println("ERROR: $anError.message")
                        }
                    })
        }
    }
}