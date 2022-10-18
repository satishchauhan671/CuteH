package com.digipanther.cuteh.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.content.pm.ActivityInfo

open class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        // comment or uncomment this line when you need your own custom exception handler for all MyActivity
       // FirebaseApp.initializeApp(this@MyActivity);
       // Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(this@MyActivity))
       // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }
}