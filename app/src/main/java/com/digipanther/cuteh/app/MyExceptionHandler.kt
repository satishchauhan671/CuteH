package com.digipanther.cuteh.app

import android.app.Activity
import android.content.Context
import android.os.Build
import android.content.Intent
import android.util.Log
import com.digipanther.cuteh.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.StringBuilder

class MyExceptionHandler(private val activity: Activity) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
      //  FirebaseCrashlytics.getInstance().log(stackTrace.toString())
       // FirebaseCrashlytics.getInstance().recordException(exception)
        exception.printStackTrace(PrintWriter(stackTrace))
        val errorReport = StringBuilder()
        errorReport.append("************ CAUSE OF ERROR ************\n\n")
        errorReport.append(stackTrace.toString())

        Log.e("CAUSE OF EXCEPTION: ", stackTrace.toString())
        errorReport.append("\n************ DEVICE INFORMATION ***********\n")
        errorReport.append("Brand: ")
        errorReport.append(Build.BRAND)
        val lineSeparator = "\n"
        errorReport.append(lineSeparator)
        errorReport.append("Device: ")
        errorReport.append(Build.DEVICE)
        errorReport.append(lineSeparator)
        errorReport.append("Model: ")
        errorReport.append(Build.MODEL)
        errorReport.append(lineSeparator)
        errorReport.append("Id: ")
        errorReport.append(Build.ID)
        errorReport.append(lineSeparator)
        errorReport.append("Product: ")
        errorReport.append(Build.PRODUCT)
        errorReport.append(lineSeparator)
        errorReport.append("\n************ FIRMWARE ************\n")
        errorReport.append("SDK: ")
        errorReport.append(Build.VERSION.SDK)
        errorReport.append(lineSeparator)
        errorReport.append("Release: ")
        errorReport.append(Build.VERSION.RELEASE)
        errorReport.append(lineSeparator)
        errorReport.append("Incremental: ")
        errorReport.append(Build.VERSION.INCREMENTAL)
        errorReport.append(lineSeparator)
        errorReport.append("App version : ")
        errorReport.append(BuildConfig.VERSION_NAME)
        errorReport.append(lineSeparator)
        sendEmail(errorReport.toString())

    }

    private fun sendEmail(error: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf("shivam.s@inventia.in", "prahlad.s@inventia.in", "satish.c@inventia.in")
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, "UGO MICI Crash Report")
        intent.putExtra(Intent.EXTRA_TEXT, error)
        activity.startActivity(Intent.createChooser(intent, ""))
        activity.finishAffinity()
    }
}