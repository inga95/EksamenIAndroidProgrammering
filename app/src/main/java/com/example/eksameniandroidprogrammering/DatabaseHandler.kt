package com.example.eksameniandroidprogrammering

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//targeting sub-requirement #8
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
