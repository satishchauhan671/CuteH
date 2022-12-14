package com.digipanther.cuteh.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.CameraActivity
import com.digipanther.cuteh.async.HotelImageAsync
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.FragmentAddHotelBinding
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.listener.ImageCallbackListener
import com.digipanther.cuteh.listener.PhotoCompressedListener
import com.digipanther.cuteh.model.HotelModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*
import java.io.File

class HotelAddEditFragment : Fragment, View.OnClickListener, ImageCallbackListener {

    private lateinit var hotelAddHotelBinding: FragmentAddHotelBinding
    private var mActivity = FragmentActivity()
    private var hotelModel: HotelModel? = null
    private var activityDashboardBinding: ActivityDashboardBinding? = null
    var hotelNameStr: String? = null
    var mobileNoStr: String? = null
    var hotelIdStr: String? = null
    var priceStr: String = "0.0"
    var roomStr: String = "0"
    var staffStr: String = "0"
    var ratingStr: String = "0"
    var tableStr: String = "0"
    var addressStr: String? = null
    var feedbackStr: String? = null
    private var imgPath: String? = null

    val REQUEST_CODE_CAMERA = 200
    val REQUEST_CODE_GALLERY = 201
    val REQUEST_PERMISSION_CAMERA = 203
    val REQUEST_PERMISSION_GALLERY = 204

    constructor()

    constructor(activityDashboardBinding: ActivityDashboardBinding) {
        this.activityDashboardBinding = activityDashboardBinding
    }

    constructor(hotelModel: HotelModel, activityDashboardBinding: ActivityDashboardBinding) {
        this.hotelModel = hotelModel
        this.activityDashboardBinding = activityDashboardBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        hotelAddHotelBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_hotel, container, false)

        hotelAddHotelBinding.saveDetailsTv.setOnClickListener(this)
        hotelAddHotelBinding.wineBeerSwitch.setOnClickListener(this)
        hotelAddHotelBinding.hotelImageLay.setOnClickListener(this)
        hotelAddHotelBinding.bookNowSwitch.setOnClickListener(this)
        hotelAddHotelBinding.toolbar.img_back.setOnClickListener(this)

        return hotelAddHotelBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityDashboardBinding!!.appBarHome.toolbar.visibility = View.GONE
        updateUi()
        if (hotelModel == null) {
            hotelModel = HotelModel()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    private fun updateUi() {
        if (hotelModel != null) {
            if (!Utility.isNullOrEmpty(hotelModel?.BUSINESS_NAME)) {
                hotelAddHotelBinding.hotelNameEt.setText(hotelModel?.BUSINESS_NAME)
            }

            if (!Utility.isNullOrEmpty(hotelModel?.MOBILE_NO)) {
                hotelAddHotelBinding.mobileNoEt.setText(hotelModel?.MOBILE_NO)
            }

            hotelAddHotelBinding.roomEt.setText(hotelModel?.ROOMS.toString())
            hotelAddHotelBinding.staffEt.setText(hotelModel?.STAFF.toString())

            if (!Utility.isNullOrEmpty(hotelModel?.RATE)) {
                hotelAddHotelBinding.ratingEt.setText(hotelModel?.RATE.toString())
            }

            hotelAddHotelBinding.tableEt.setText(hotelModel?.DINE_TABLE.toString())
            hotelAddHotelBinding.priceEt.setText(hotelModel?.PRICE.toString())



            if (!Utility.isNullOrEmpty(hotelModel?.BOOK_NOW)) {
                hotelAddHotelBinding.bookNowSwitch.isChecked = true
                hotelAddHotelBinding.bookNowSwitch.trackTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                hotelAddHotelBinding.bookNowSwitch.thumbTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
            } else {
                hotelAddHotelBinding.bookNowSwitch.isChecked = false
                hotelAddHotelBinding.bookNowSwitch.trackTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                hotelAddHotelBinding.bookNowSwitch.thumbTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
            }

            if (!Utility.isNullOrEmpty(hotelModel?.WINE_BEER_AVAILABLE)) {
                hotelAddHotelBinding.wineBeerSwitch.isChecked = true
                hotelAddHotelBinding.wineBeerSwitch.trackTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                hotelAddHotelBinding.wineBeerSwitch.thumbTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
            } else {
                hotelAddHotelBinding.wineBeerSwitch.isChecked = false
                hotelAddHotelBinding.wineBeerSwitch.trackTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                hotelAddHotelBinding.wineBeerSwitch.thumbTintList =
                    ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
            }

            if (!Utility.isNullOrEmpty(hotelModel?.ADDRESS)) {
                hotelAddHotelBinding.addressEt.setText(hotelModel?.ADDRESS)
            }

            if (!Utility.isNullOrEmpty(hotelModel?.FEEDBACK)) {
                hotelAddHotelBinding.feedbackEt.setText(hotelModel?.FEEDBACK)
            }

            if (!Utility.isNullOrEmpty(hotelModel?.IMAGE_PATH)){
                imgPath = hotelModel?.IMAGE_PATH
                hotelAddHotelBinding.hotelIv.visibility = View.VISIBLE
                hotelAddHotelBinding.hotelIv.setImageBitmap(
                    Utility.getBitmapByStringImage(
                        hotelModel?.IMAGE_PATH
                    )
                )
            }

            if (!Utility.isNullOrEmpty(hotelModel?.HOTEL_ID)){
                hotelIdStr = hotelModel?.HOTEL_ID
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.saveDetailsTv -> {
                hotelNameStr = hotelAddHotelBinding.hotelNameEt.text.toString().trim()
                mobileNoStr = hotelAddHotelBinding.mobileNoEt.text.toString().trim()
                priceStr = hotelAddHotelBinding.priceEt.text.toString().trim()
                roomStr = hotelAddHotelBinding.roomEt.text.toString().trim()
                staffStr = hotelAddHotelBinding.staffEt.text.toString().trim()
                ratingStr = hotelAddHotelBinding.ratingEt.text.toString().trim()
                tableStr = hotelAddHotelBinding.tableEt.text.toString().trim()
                addressStr = hotelAddHotelBinding.addressEt.text.toString().trim()
                feedbackStr = hotelAddHotelBinding.feedbackEt.text.toString().trim()


                hotelModel?.MOBILE_NO = mobileNoStr
                hotelModel?.HOTEL_ID = hotelIdStr
                hotelModel?.PRICE = priceStr
                hotelModel?.RATE = ratingStr
                hotelModel?.ADDRESS = addressStr
                hotelModel?.FEEDBACK = feedbackStr
                hotelModel?.BUSINESS_NAME = hotelNameStr
                hotelModel?.IMAGE_PATH = imgPath


                if (!Utility.isNullOrEmpty(roomStr)) {
                    hotelModel?.ROOMS = roomStr.toInt()
                }
                if (!Utility.isNullOrEmpty(staffStr)) {
                    hotelModel?.STAFF = staffStr.toInt()
                }
                if (Utility.isNullOrEmpty(hotelModel?.HOTEL_ID)){
                    hotelIdStr = Utility.getHotelId("1")
                }
                if (!Utility.isNullOrEmpty(tableStr)) {
                    hotelModel?.DINE_TABLE = tableStr.toInt()
                }


                if (Utility.isNullOrEmpty(hotelNameStr)){
                    Utility.snackBar(view,"Please Enter Hotel Name",2000, mActivity.resources.getColor(R.color.warning,mActivity.theme))
                }else if (Utility.isNullOrEmpty(imgPath)){
                    Utility.snackBar(view,"Please Add Hotel Image",2000, mActivity.resources.getColor(R.color.warning,mActivity.theme))
                }else{
                    var isSaved: Boolean = HotelDataHelper.saveHotelData(hotelModel!!, mActivity)
                    if (isSaved) {
                        Utility.snackBar(view,
                            "Data Saved Successfully",
                            2000,
                            mActivity.resources.getColor(R.color.color_green))

                        Utility.replaceFragment(HotelFragment(
                            activityDashboardBinding!!),
                            mActivity.supportFragmentManager,
                            R.id.layout_fragment)
                    }
                }


            }

            R.id.hotelImageLay -> {
                val onOptionCLickListener: GalleryBottomSheet.OnOptionCLickListener =
                    object : GalleryBottomSheet.OnOptionCLickListener {
                        override fun onManageClick(optionId: Int) {
                            if (optionId == 0) {
                                checkCameraPermission()
                            } else if (optionId == 1) {
                                checkExternalPermission()
                            }
                        }

                        override fun onDialogOpened(optionId: Boolean) {

                        }

                    }


                val feederGalleryBottomSheet = GalleryBottomSheet(onOptionCLickListener)
                feederGalleryBottomSheet.show(
                    mActivity.supportFragmentManager,
                    feederGalleryBottomSheet.tag
                )
            }

            R.id.wineBeerSwitch -> {
                if (hotelAddHotelBinding.wineBeerSwitch.isChecked) {
                    hotelAddHotelBinding.wineBeerSwitch.trackTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                    hotelAddHotelBinding.wineBeerSwitch.thumbTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                } else {
                    hotelAddHotelBinding.wineBeerSwitch.trackTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                    hotelAddHotelBinding.wineBeerSwitch.thumbTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                }
            }

            R.id.bookNowSwitch -> {
                if (hotelAddHotelBinding.bookNowSwitch.isChecked) {
                    hotelAddHotelBinding.bookNowSwitch.trackTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                    hotelAddHotelBinding.bookNowSwitch.thumbTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                } else {
                    hotelAddHotelBinding.bookNowSwitch.trackTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                    hotelAddHotelBinding.bookNowSwitch.thumbTintList =
                        ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                }
            }

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                mActivity!!,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        ) {
            // Open Camera Intent
            CameraFragment.bothlensFacing = true
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra("pageAction", true)
            startActivity(intent)

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    android.Manifest.permission.CAMERA
                )
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CAMERA
                )
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CAMERA
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (data != null && data.extras != null) {
                showImageData(resultCode, data)
            }
        }

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (data != null && data.data != null) {
                showImageData(resultCode, data)
            }
        }
    }

    private fun checkExternalPermission() {
        if (ContextCompat.checkSelfPermission(
                mActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Pick Image From Gallery Intent
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_CODE_GALLERY)
        } else {


            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_GALLERY
                )
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_GALLERY
                )
            }
        }
    }


    private fun showImageData(resultCode: Int, data: Intent?) {
        if (data != null && data.data != null && resultCode == Activity.RESULT_OK) {
            var isImageFromGoogleDrive = false
            val uri = data.data
            if ("content".equals(uri!!.scheme, ignoreCase = true)) {
                var cursor: Cursor? = null
                val column = "_data"
                val projection = arrayOf(column)
                try {
                    cursor = mActivity.contentResolver.query(uri, projection, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val column_index = cursor.getColumnIndexOrThrow(column)
                        imgPath = cursor.getString(column_index)
                    }
                } finally {
                    cursor?.close()
                }
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                imgPath = uri.path
            }
            if (isImageFromGoogleDrive) {
                try {
                    try {
                        val file: File = File(imgPath)
                        if (file.exists()) {
                            HotelImageAsync(
                                file,
                                hotelModel!!,
                                object : PhotoCompressedListener {
                                    override fun compressedPhoto(path: String?) {
                                        if (path != null) {

                                            val rotation = Utility.getRotation(path)
                                            imgPath = Utility.bitmapToBASE64(
                                                Utility.rotateImage(
                                                    Utility.getBitmap(path)!!, rotation.toFloat()
                                                ))


                                            hotelAddHotelBinding.hotelIv.visibility =
                                                View.VISIBLE
                                            hotelAddHotelBinding.hotelIv.setImageBitmap(
                                                Utility.getBitmapByStringImage(
                                                    imgPath
                                                )
                                            )
                                        }
                                    }
                                }).execute()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("CKDemo", "Exception in photo callback")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    val file: File = File(imgPath)
                    if (file.exists()) {
                        HotelImageAsync(
                            file,
                            hotelModel!!,
                            object : PhotoCompressedListener {
                                override fun compressedPhoto(path: String?) {
                                    if (hotelModel != null) {
                                        if (!Utility.isNullOrEmpty(path)) {

                                            val rotation = Utility.getRotation(path)
                                            imgPath = Utility.bitmapToBASE64(
                                                Utility.rotateImage(
                                                    Utility.getBitmap(path)!!, rotation.toFloat()
                                                ))

                                            hotelAddHotelBinding.hotelIv.visibility =
                                                View.VISIBLE
                                            hotelAddHotelBinding.hotelIv.setImageBitmap(
                                                Utility.getBitmapByStringImage(
                                                    imgPath
                                                )
                                            )
                                        }
                                    }
                                }
                            }).execute()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("CKDemo", "Exception in photo callback")
                }
            }
        }
    }

    override fun imageCallback(file: File) {
        try {
            //  Utility.playBeep(mActivity, null);
            if (file.exists() && hotelModel != null) {
                HotelImageAsync(
                    file,
                    hotelModel!!,
                    object : PhotoCompressedListener {
                        override fun compressedPhoto(path: String?) {
                            if (hotelModel != null) {
                                if (path != null) {

                                    val rotation = Utility.getRotation(path)
                                    imgPath = Utility.bitmapToBASE64(
                                        Utility.rotateImage(
                                            Utility.getBitmap(path)!!, rotation.toFloat()
                                        ))


                                    hotelAddHotelBinding.hotelIv.visibility =
                                        View.VISIBLE
                                    hotelAddHotelBinding.hotelIv.setImageBitmap(
                                        Utility.getBitmapByStringImage(
                                            imgPath
                                        )
                                    )
                                }
                            }
                        }
                    }).execute()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("CKDemo", "Exception in photo callback")
        }
    }

}