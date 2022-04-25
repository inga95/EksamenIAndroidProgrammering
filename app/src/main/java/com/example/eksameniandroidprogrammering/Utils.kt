
package com.example.eksameniandroidprogrammering

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class Utils : AppCompatActivity() {

    private var selectedImage: Uri? = null
    lateinit  var context: Context
    lateinit var bitmap : Bitmap


    fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
        val selectedImage: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return selectedImage
    }


    fun bitmapToByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun bitmapToFile(bitmap : Bitmap, filename : String, context: Context) : File{

        val file = File(context.getCacheDir(), filename)
        file.createNewFile()

        val bitmapdata = bitmapToByteArray(bitmap)

        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(bitmapdata)
        fileOutputStream.flush()
        fileOutputStream.close()

        return file
    }

    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }


}

























/*
fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}


fun maybeGetFile() {
    if (selectedImage == null) {
        val toast = Toast.makeText(
            applicationContext,
            "you need to select an image",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
    val parcelFileDescriptor =
        contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return
    val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
}*/

