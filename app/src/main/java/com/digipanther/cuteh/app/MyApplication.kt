package com.digipanther.cuteh.app

import android.app.Application
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import java.lang.Exception
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.jvm.Synchronized
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
         createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        val TAG = MyApplication::class.java.simpleName!!
        private val context: WeakReference<Context>? = null
        const val CHANNEL_ID = "CIServiceChannel"
        @get:Synchronized
        var mInstance: MyApplication? = null
            private set
        fun checkExpireTime(): Boolean {
            // Login loginIBILL = Login_Helper.getLogin(MyApplication.getInstance());
            try {
                val formatter = SimpleDateFormat("dd-MMM-yyyy")

            } catch (e: Exception) {
                e.message
            }
            return false
        }

        fun checkAPPRelease(): Boolean {
            return false
        }
    }
}