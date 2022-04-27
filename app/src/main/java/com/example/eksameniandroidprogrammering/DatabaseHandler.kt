package com.example.eksameniandroidprogrammering

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.sql.Blob

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ImageDB.db"
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("create table Images (id primary key, image blob)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("drop table if exists images")
        onCreate(db)

    }



}





/*

CODE FOR WORKING DATABSE INPUT IN ONACTIVITY

    fun saveImage(requestCode: Int, resultCode: Int, data: Intent?) {
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
            println(bitmap)
            val byteArray = utils.bitmapToByteArray(bitmap)
            println(byteArray)

            println("you got further" + byteArray)


            dbHandler.writableDatabase.insert("Images", null, ContentValues().apply {
                put("image", byteArray)
            })
            println("you got to the end")


        }
    }*/



    /*fun addImages(img: DataModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_DATA, img.data)

        val sucess = db.insert(TABLE_IMAGES, null, contentValues)

        db.close()
        return sucess
    }*/

    /*fun viewImages(): ArrayList<DataModel> {
        val imgList: ArrayList<DataModel> = ArrayList<DataModel>()

        val selectQuery = "SELECT * FROM $TABLE_IMAGES"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var image: Blob

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                image = cursor.getBlob(cursor.getColumnIndex(KEY_DATA))

                val img = DataModel(id = id, data = image)
                imgList.add(img)
            } while (cursor.moveToNext())
        }
        return imgList
    }*/


