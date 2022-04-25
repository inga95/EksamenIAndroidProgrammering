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
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "ImageDB.db"


            /*private const val TABLE_IMAGES = "ImagesTable"
            private const val KEY_ID = "ID"
            private const val KEY_DATA = "Images"*/
        }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("create table images (id primary key, image blob)")

        /*val CREATE_IMAGE_TABLE = ("CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATA + " BLOB" + ")")
        db?.execSQL(CREATE_IMAGE_TABLE)*/
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("drop table if exists images")
        onCreate(db)

        /*db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES)
        onCreate(db)*/
    }

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


}