package com.example.eksameniandroidprogrammering

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class Services : Service() {

    private var binder: IBinder? = ServicesBinder()

    inner class ServicesBinder : Binder() {
        fun getService(): Services = this@Services
    }


    override fun onBind(p0: Intent?): IBinder? {
        println("service onBind")
        return binder
    }
}