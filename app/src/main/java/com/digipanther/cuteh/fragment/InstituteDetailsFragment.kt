package com.digipanther.cuteh.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.digipanther.cuteh.R
import com.digipanther.cuteh.adapter.HotelAdapter
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.FragmentHotelBinding
import com.digipanther.cuteh.databinding.FragmentHotelDetailsBinding
import com.digipanther.cuteh.databinding.FragmentInstituteDetailsBinding
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.model.HotelModel
import com.digipanther.cuteh.model.InstituteModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*

class InstituteDetailsFragment : Fragment, View.OnClickListener {

    private lateinit var instituteDetailsBinding: FragmentInstituteDetailsBinding
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private var mActivity = FragmentActivity()
    private lateinit var instituteModel : InstituteModel
    private var emptyVal: String = "N/A"

    constructor() : super()

    constructor(instituteModel: InstituteModel, dashboardBinding: ActivityDashboardBinding) {
        this.dashboardBinding = dashboardBinding
        this.instituteModel = instituteModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        instituteDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_institute_details, container, false)

        return instituteDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardBinding.appBarHome.toolbar.visibility = View.GONE
        instituteDetailsBinding.toolbar.img_back.setOnClickListener(this)
        instituteDetailsBinding.editDetailsTv.setOnClickListener(this)
        updateUi()
    }

    private fun updateUi() {
        if (instituteModel != null) {

            if (!Utility.isNullOrEmpty(instituteModel.COLLEGE_NAME)) {

                if (!Utility.isNullOrEmpty(instituteModel.COLLEGE_ID)) {
                    instituteDetailsBinding.collageNameTv.text =
                        "${instituteModel.COLLEGE_NAME} (${instituteModel.COLLEGE_ID})"
                } else {
                    instituteDetailsBinding.collageNameTv.text = instituteModel.COLLEGE_NAME
                }
            }

            if (!Utility.isNullOrEmpty(instituteModel.MOBILE_NO)) {
                instituteDetailsBinding.mobileNoTv.text = instituteModel.MOBILE_NO
            } else {
                instituteDetailsBinding.mobileNoTv.text = emptyVal
            }

            if (!Utility.isNullOrEmpty(instituteModel.COURSE)) {
                instituteDetailsBinding.courseTv.text = instituteModel.COURSE
            } else {
                instituteDetailsBinding.courseTv.text = emptyVal
            }

            instituteDetailsBinding.collageFeeTv.text = instituteModel.TUITION_FEES.toString()

            if (!Utility.isNullOrEmpty(instituteModel.BRANCH)){
                instituteDetailsBinding.branchTv.text = instituteModel.BRANCH
            }else{
                instituteDetailsBinding.branchTv.text = emptyVal
            }

            if (!Utility.isNullOrEmpty(instituteModel.SUBJECT)){
                instituteDetailsBinding.subjectTv.text = instituteModel.SUBJECT
            }else{
                instituteDetailsBinding.subjectTv.text = emptyVal
            }


            if (!Utility.isNullOrEmpty(instituteModel.QUERY_FROM)){
                instituteDetailsBinding.queryFormTv.text = instituteModel.QUERY_FROM
            }else{
                instituteDetailsBinding.queryFormTv.text = emptyVal
            }

            if (!Utility.isNullOrEmpty(instituteModel.FEEDBACK)){
                instituteDetailsBinding.feedbackTv.text = instituteModel.FEEDBACK
            }else{
                instituteDetailsBinding.feedbackTv.text = emptyVal
            }

            if (!Utility.isNullOrEmpty(instituteModel.IMAGE_PATH)){
                instituteDetailsBinding.collageImg.setImageBitmap(
                    Utility.getBitmapByStringImage(
                        instituteModel.IMAGE_PATH
                    )
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.editDetailsTv -> {
                Utility.replaceFragment(InstituteAddEditFragment(instituteModel, dashboardBinding),
                    mActivity.supportFragmentManager,
                    R.id.layout_fragment)
            }

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }


}