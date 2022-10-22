package com.digipanther.cuteh.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.CameraActivity
import com.digipanther.cuteh.async.InstituteImageAsync
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.FragmentAddInstituteBinding
import com.digipanther.cuteh.dbHelper.InstituteDataHelper
import com.digipanther.cuteh.listener.ImageCallbackListener
import com.digipanther.cuteh.listener.PhotoCompressedListener
import com.digipanther.cuteh.model.InstituteModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*
import java.io.File


class InstituteAddEditFragment : Fragment, View.OnClickListener, ImageCallbackListener {

    private lateinit var hotelAddInstituteBinding: FragmentAddInstituteBinding
    private var mActivity = FragmentActivity()
    private var instituteModel: InstituteModel? = null
    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    var instituteNameStr: String? = null
    var mobileNoStr: String? = null
    var collageFeeStr: String? = null
    var courseStr: String? = null
    var branchStr: String? = null
    var subjectStr: String? = null
    var queryFormStr: String? = null
    var feedbackStr: String? = null
    var instituteIdStr: String? = null
    var feeTypeStr: String? = null

    private var imgPath: String? = null

    val REQUEST_CODE_CAMERA = 200
    val REQUEST_CODE_GALLERY = 201
    val REQUEST_PERMISSION_CAMERA = 203
    val REQUEST_PERMISSION_GALLERY = 204

    constructor()

    constructor(
        activityDashboardBinding: ActivityDashboardBinding,
    ) {
        this.activityDashboardBinding = activityDashboardBinding
    }

    constructor(
        instituteModel: InstituteModel,
        activityDashboardBinding: ActivityDashboardBinding,
    ) {
        this.instituteModel = instituteModel
        this.activityDashboardBinding = activityDashboardBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        hotelAddInstituteBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_institute, container, false)
        hotelAddInstituteBinding.toolbar.img_back.setOnClickListener(this)
        hotelAddInstituteBinding.saveDetailsTv.setOnClickListener(this)
        hotelAddInstituteBinding.collageImageLay.setOnClickListener(this)

        return hotelAddInstituteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        if (instituteModel == null) {
            instituteModel = InstituteModel()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    private fun updateUi() {
        if (instituteModel != null) {
            if (!Utility.isNullOrEmpty(instituteModel?.COLLEGE_NAME)) {
                hotelAddInstituteBinding.collageNameEt.setText(instituteModel?.COLLEGE_NAME)
            }

            if (!Utility.isNullOrEmpty(instituteModel?.MOBILE_NO)) {
                hotelAddInstituteBinding.mobileNoEt.setText(instituteModel?.MOBILE_NO)
            }

            if (!Utility.isNullOrEmpty(instituteModel?.COURSE)) {
                hotelAddInstituteBinding.courseEt.setText(instituteModel?.COURSE)
            }

            if (!Utility.isNullOrEmpty(instituteModel?.BRANCH)) {
                hotelAddInstituteBinding.branchEt.setText(instituteModel?.BRANCH.toString())
            }

            if (!Utility.isNullOrEmpty(instituteModel?.SUBJECT)) {
                hotelAddInstituteBinding.subjectEt.setText(instituteModel?.SUBJECT.toString())
            }


            hotelAddInstituteBinding.collageFeeEt.setText(instituteModel?.TUITION_FEES.toString())

            if (!Utility.isNullOrEmpty(instituteModel?.QUERY_FROM)) {
                hotelAddInstituteBinding.queryFormEt.setText(instituteModel?.QUERY_FROM.toString())
            }

            if (!Utility.isNullOrEmpty(instituteModel?.FEEDBACK)) {
                hotelAddInstituteBinding.feedbackEt.setText(instituteModel?.FEEDBACK.toString())
            }

            if (!Utility.isNullOrEmpty(instituteModel?.IMAGE_PATH)){
                imgPath = instituteModel?.IMAGE_PATH
                hotelAddInstituteBinding.collageImg.visibility = View.VISIBLE
                hotelAddInstituteBinding.collageImg.setImageBitmap(
                    Utility.getBitmapByStringImage(
                        instituteModel?.IMAGE_PATH
                    )
                )
            }

            if (!Utility.isNullOrEmpty(instituteModel?.COLLEGE_ID)){
                instituteIdStr = instituteModel?.COLLEGE_ID
            }


            val feeTypeList = mActivity.resources.getStringArray(R.array.feeType)
            val arrayAdapter = ArrayAdapter(mActivity, R.layout.layout_text,feeTypeList)
            arrayAdapter.setDropDownViewResource(R.layout.layout_text)
            hotelAddInstituteBinding.spinnerFeeType.adapter = arrayAdapter
            hotelAddInstituteBinding.spinnerFeeType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    feeTypeStr = feeTypeList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.saveDetailsTv -> {
                instituteNameStr = hotelAddInstituteBinding.collageNameEt.text.toString().trim()
                mobileNoStr = hotelAddInstituteBinding.mobileNoEt.text.toString().trim()

                collageFeeStr = hotelAddInstituteBinding.collageFeeEt.text.toString().trim()
                courseStr = hotelAddInstituteBinding.courseEt.text.toString().trim()
                branchStr = hotelAddInstituteBinding.branchEt.text.toString().trim()
                subjectStr = hotelAddInstituteBinding.subjectEt.text.toString().trim()
                queryFormStr = hotelAddInstituteBinding.queryFormEt.text.toString().trim()
                feedbackStr = hotelAddInstituteBinding.feedbackEt.text.toString().trim()

                instituteModel?.COLLEGE_NAME = instituteNameStr
                instituteModel?.MOBILE_NO = mobileNoStr
                instituteModel?.COLLEGE_ID = instituteIdStr
                instituteModel?.COURSE = courseStr
                if (!Utility.isNullOrEmpty(collageFeeStr)) {
                    instituteModel?.TUITION_FEES = collageFeeStr!!.toDouble()
                }
                if (Utility.isNullOrEmpty(instituteModel?.COLLEGE_ID)){
                    instituteIdStr = Utility.getCollageId("1")
                }else{
                    instituteIdStr = instituteModel?.COLLEGE_ID
                }
                instituteModel?.BRANCH = branchStr
                instituteModel?.SUBJECT = subjectStr
                instituteModel?.QUERY_FROM = queryFormStr
                instituteModel?.FEEDBACK = feedbackStr
                instituteModel?.FEETYPE = feeTypeStr
                instituteModel?.IMAGE_PATH = imgPath

                if (Utility.isNullOrEmpty(instituteNameStr)){
                    Utility.snackBar(view,"Please Enter Collage Name",2000, mActivity.resources.getColor(R.color.warning,mActivity.theme))
                }else if (Utility.isNullOrEmpty(imgPath)){
                    Utility.snackBar(view,"Please Add Hotel Image",2000, mActivity.resources.getColor(R.color.warning,mActivity.theme))
                }else{
                    var isSaved: Boolean =
                        InstituteDataHelper.saveCollageData(instituteModel!!, mActivity)
                    if (isSaved) {
                        Utility.snackBar(view,
                            "Data Saved Successfully",
                            2000,
                            mActivity.resources.getColor(R.color.color_green))


                        Utility.replaceFragment(InstituteFragment(
                            activityDashboardBinding),
                            mActivity.supportFragmentManager,
                            R.id.layout_fragment)
                    }
                }

            }

            R.id.collageImageLay -> {
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

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                mActivity,
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
                            InstituteImageAsync(
                                file,
                                instituteModel!!,
                                object : PhotoCompressedListener {
                                    override fun compressedPhoto(path: String?) {
                                        if (path != null) {

                                            val rotation = Utility.getRotation(path)
                                            imgPath = Utility.bitmapToBASE64(
                                                Utility.rotateImage(
                                                    Utility.getBitmap(path)!!, rotation.toFloat()
                                                ))


                                            hotelAddInstituteBinding.collageImg.visibility =
                                                View.VISIBLE
                                            hotelAddInstituteBinding.collageImg.setImageBitmap(
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
                        InstituteImageAsync(
                            file,
                            instituteModel!!,
                            object : PhotoCompressedListener {
                                override fun compressedPhoto(path: String?) {
                                    if (instituteModel != null) {
                                        if (path != null) {

                                            val rotation = Utility.getRotation(path)
                                            imgPath = Utility.bitmapToBASE64(
                                                Utility.rotateImage(
                                                    Utility.getBitmap(path)!!, rotation.toFloat()
                                                ))


                                            hotelAddInstituteBinding.collageImg.visibility =
                                                View.VISIBLE
                                            hotelAddInstituteBinding.collageImg.setImageBitmap(
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
            if (file.exists() && instituteModel != null) {
                InstituteImageAsync(
                    file,
                    instituteModel!!,
                    object : PhotoCompressedListener {
                        override fun compressedPhoto(path: String?) {
                            if (instituteModel != null) {
                                if (path != null) {


                                    val rotation = Utility.getRotation(path)
                                    imgPath = Utility.bitmapToBASE64(
                                        Utility.rotateImage(
                                            Utility.getBitmap(path)!!, rotation.toFloat()
                                        ))


                                    hotelAddInstituteBinding.collageImg.visibility =
                                        View.VISIBLE
                                    hotelAddInstituteBinding.collageImg.setImageBitmap(
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