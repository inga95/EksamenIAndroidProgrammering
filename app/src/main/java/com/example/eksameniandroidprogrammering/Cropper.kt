package com.example.eksameniandroidprogrammering

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

class Cropper : AppCompatActivity() {
    var result: String? = null
    var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readIntent()
        val options = UCrop.Options()
        val dest_uri = StringBuilder(UUID.randomUUID().toString()).append(".png").toString()
        UCrop.of(fileUri!!, Uri.fromFile(File(cacheDir, dest_uri)))
            .withOptions(options)
            .withAspectRatio(0f, 0f)
            .useSourceImageAspectRatio()
            .withMaxResultSize(2000, 2000)
            .start(this@Cropper)
    }

    private fun readIntent() {
        val intent = intent
        if (intent.extras != null) {
            result = intent.getStringExtra("DATA")
            fileUri = Uri.parse(result)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val returnIntent = Intent()
            returnIntent.putExtra("RESULT", resultUri.toString() + "")
            setResult(-1, returnIntent)
            finish()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
        }
    }
}