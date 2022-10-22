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
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.model.HotelModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*

class HotelDetailsFragment : Fragment, View.OnClickListener {

    private lateinit var hotelDetailsBinding: FragmentHotelDetailsBinding
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private var mActivity = FragmentActivity()
    private lateinit var hotelModel: HotelModel
    private var emptyVal: String = "N/A"

    constructor() : super()

    constructor(hotelModel: HotelModel, dashboardBinding: ActivityDashboardBinding) {
        this.dashboardBinding = dashboardBinding
        this.hotelModel = hotelModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        hotelDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hotel_details, container, false)

        return hotelDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardBinding.appBarHome.toolbar.visibility = View.GONE
        hotelDetailsBinding.whiteToolbar.img_back.setOnClickListener(this)
        hotelDetailsBinding.editDetailsTv.setOnClickListener(this)
        updateUi()
    }

    private fun updateUi() {
        if (hotelModel != null) {

            if (!Utility.isNullOrEmpty(hotelModel.BUSINESS_NAME)) {

                if (!Utility.isNullOrEmpty(hotelModel.HOTEL_ID)) {
                    hotelDetailsBinding.hotelNameTv.text =
                        "${hotelModel.BUSINESS_NAME} (${hotelModel.HOTEL_ID})"
                } else {
                    hotelDetailsBinding.hotelNameTv.text = hotelModel.BUSINESS_NAME
                }
            }

            if (!Utility.isNullOrEmpty(hotelModel.MOBILE_NO)) {
                hotelDetailsBinding.mobileNoTv.text = hotelModel.MOBILE_NO
            } else {
                hotelDetailsBinding.mobileNoTv.text = emptyVal
            }

            hotelDetailsBinding.roomTv.text = hotelModel.ROOMS.toString()
            hotelDetailsBinding.staffTv.text = hotelModel.STAFF.toString()

            if (!Utility.isNullOrEmpty(hotelModel.RATE)){
                hotelDetailsBinding.ratingTv.text = hotelModel.RATE.toString()
            }else{
                hotelDetailsBinding.ratingTv.text = emptyVal
            }

            hotelDetailsBinding.tableTv.text = hotelModel.DINE_TABLE.toString()
            hotelDetailsBinding.priceTv.text = hotelModel.PRICE.toString()

            hotelDetailsBinding.bookNowSwitch.isEnabled = false
            hotelDetailsBinding.wineBeerSwitch.isEnabled = false

            if (!Utility.isNullOrEmpty(hotelModel.IMAGE_PATH)){
                hotelDetailsBinding.hotelIv.setImageBitmap(
                    Utility.getBitmapByStringImage(
                        hotelModel.IMAGE_PATH
                    )
                )
            }

            if (!Utility.isNullOrEmpty(hotelModel.BOOK_NOW)){
                hotelDetailsBinding.bookNowSwitch.isChecked = true
                hotelDetailsBinding.bookNowSwitch.trackTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                hotelDetailsBinding.bookNowSwitch.thumbTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
            }else{
                hotelDetailsBinding.bookNowSwitch.isChecked = false
                hotelDetailsBinding.bookNowSwitch.trackTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                hotelDetailsBinding.bookNowSwitch.thumbTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
            }

            if (!Utility.isNullOrEmpty(hotelModel.WINE_BEER_AVAILABLE)){
                hotelDetailsBinding.wineBeerSwitch.isChecked = true
                hotelDetailsBinding.wineBeerSwitch.trackTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
                hotelDetailsBinding.wineBeerSwitch.thumbTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.colorPrimary))
            }else{
                hotelDetailsBinding.wineBeerSwitch.isChecked = false
                hotelDetailsBinding.wineBeerSwitch.trackTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
                hotelDetailsBinding.wineBeerSwitch.thumbTintList = ColorStateList.valueOf(mActivity.resources.getColor(R.color.color_747474))
            }

            if (!Utility.isNullOrEmpty(hotelModel.ADDRESS)){
                hotelDetailsBinding.addressTv.text = hotelModel.ADDRESS
            }else{
                hotelDetailsBinding.addressTv.text = emptyVal
            }

            if (!Utility.isNullOrEmpty(hotelModel.FEEDBACK)){
                hotelDetailsBinding.feedbackTv.text = hotelModel.RATE
            }else{
                hotelDetailsBinding.feedbackTv.text = emptyVal
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
                Utility.replaceFragment(HotelAddEditFragment(hotelModel, dashboardBinding),
                    mActivity.supportFragmentManager,
                    R.id.layout_fragment)
            }

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }


}