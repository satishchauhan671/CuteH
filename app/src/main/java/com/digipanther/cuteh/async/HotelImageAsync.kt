package com.digipanther.cuteh.async

import android.content.Context
import android.os.AsyncTask
import com.digipanther.cuteh.app.MyApplication
import com.digipanther.cuteh.common.Utility.bitmapToBASE64
import com.digipanther.cuteh.common.Utility.dateStringWithTime
import com.digipanther.cuteh.common.Utility.deleteImages
import com.digipanther.cuteh.common.Utility.deleteSignature
import com.digipanther.cuteh.common.Utility.getBitmap
import com.digipanther.cuteh.common.Utility.getRotation
import com.digipanther.cuteh.common.Utility.getStringPreference
import com.digipanther.cuteh.common.Utility.rotateImage
import com.digipanther.cuteh.common.Utility.setPic
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.IMAGE_PATH
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.listener.PhotoCompressedListener
import com.digipanther.cuteh.model.HotelModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class HotelImageAsync : AsyncTask<Void?, Void?, String?> {
    private var maxHeight = 500
    private var maxWidth = 500
    private var path: String?
    private val context: WeakReference<Context>? = null
    private var photoCompressedListener: PhotoCompressedListener?
    private var hotelModel: HotelModel
    private var photoPath: File

    constructor(
        photoPath: File,
        hotelModel: HotelModel,
        photoCompressedListener: PhotoCompressedListener?
    ) {
        this.path = photoPath.absolutePath
        this.photoPath = photoPath
        this.hotelModel = hotelModel
        this.photoCompressedListener = photoCompressedListener
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg voids: Void?): String? {
        if (photoPath.exists() && path != null) {

            val myDir: File = MyApplication.mInstance!!.applicationContext.filesDir
            val externalDirectory: File? =
                MyApplication.mInstance!!.applicationContext.getExternalFilesDir(path)
            val documentsFolder = File(myDir, "CuteH")
            if (!documentsFolder.exists()) {
                documentsFolder.mkdirs()
            }
            try {
                val destination = File(documentsFolder.path + "/" + externalDirectory?.name)
                val src = FileInputStream(photoPath).channel
                val dst = FileOutputStream(destination).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val numFiles: File = File(
                MyApplication.mInstance!!.applicationContext.filesDir.absolutePath
                    .toString() + "/CuteH" + "/" + externalDirectory?.name
            )
            path = numFiles.path
            photoPath = numFiles


            val rotation = getRotation(path)
            if (photoPath.length() / 1024 > 200) {
                while (photoPath.length() / 1024 > 200) {
                    setPic(path, maxHeight, maxWidth)
                    maxHeight = maxHeight - 100
                    maxWidth = maxWidth - 100
                }
            }
            hotelModel.IMAGE_PATH = bitmapToBASE64(
                rotateImage(
                    getBitmap(path)!!, rotation.toFloat()
                )
            )

                /*if (photoPath.length() / 1024 > 20) {
                    while (photoPath.length() / 1024 > 20) {
                        setPic(path, maxHeight, maxWidth)
                        maxHeight = maxHeight - 100
                        maxWidth = maxWidth - 100
                    }



                    hotelModel.LATITUDE = getStringPreference("latitude")
                    hotelModel.LONGITUDE = getStringPreference("longitude")
//                    hotelModel.VISITED_DATE =
//                        dateStringWithTime(Date(System.currentTimeMillis()))

            }
                else {
                val rotation = getRotation(path)
                if (photoPath.length() / 1024 > 200) {
                    while (photoPath.length() / 1024 > 200) {
                        setPic(path, maxHeight, maxWidth)
                        maxHeight = maxHeight - 100
                        maxWidth = maxWidth - 100
                    }
                }
                hotelModel.IMAGE_PATH = bitmapToBASE64(
                    rotateImage(
                        getBitmap(path)!!, rotation.toFloat()
                    )
                )
            }*/
            try {
                deleteImages(MyApplication.mInstance!!)
                deleteSignature(MyApplication.mInstance!!)
            } catch (e: Exception) {
                e.message
            }
        }
        return path
    }

    override fun onPostExecute(path: String?) {
        super.onPostExecute(path)
        if (photoCompressedListener != null) photoCompressedListener!!.compressedPhoto(path)
    }


}