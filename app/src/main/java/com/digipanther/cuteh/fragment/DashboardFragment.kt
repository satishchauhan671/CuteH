package com.digipanther.cuteh.fragment

import android.content.Context
import android.icu.lang.UCharacter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.DashboardActivity
import com.digipanther.cuteh.adapter.HotelAdapter
import com.digipanther.cuteh.adapter.InstituteAdapter
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.AppBarHomeBinding
import com.digipanther.cuteh.databinding.FragmentDashboardBinding
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.dbHelper.InstituteDataHelper
import com.digipanther.cuteh.dbHelper.UserDataHelper
import com.digipanther.cuteh.model.HotelModel
import com.digipanther.cuteh.model.InstituteModel
import com.digipanther.cuteh.model.UserInfoModel
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment {
    private lateinit var activityDashboardBinding : ActivityDashboardBinding
    private lateinit var dashboardFragmentBinding: FragmentDashboardBinding
    private lateinit var appbarMainBinding: AppBarHomeBinding
    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var instituteAdapter: InstituteAdapter
    private var mActivity = FragmentActivity()

    constructor() : super()

    constructor(activityDashboardBinding: ActivityDashboardBinding){
        this.activityDashboardBinding = activityDashboardBinding
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        super.onCreateView(inflater, container, savedInstanceState)
        activityDashboardBinding.appBarHome.toolbar.visibility = View.VISIBLE
        dashboardFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dashboard, container, false)

        activityDashboardBinding.appBarHome.contentMain.bottomNavigation.visibility =
            View.VISIBLE
        appbarMainBinding = activityDashboardBinding.appBarHome

        val toolbar = (mActivity as DashboardActivity?)!!.supportActionBar
        toolbar!!.title = "Dashboard"
        toolbar.subtitle = ""


        val infoModel : UserInfoModel? = UserDataHelper.getLogin(mActivity)
        if (infoModel != null){
            if (!Utility.isNullOrEmpty(infoModel.userName)){
                if (infoModel.userName?.contains(" ") == true){
                    dashboardFragmentBinding.nameTv.text = Utility.toTitleCase(infoModel.userName)
                }else{
                    dashboardFragmentBinding.nameTv.text = "${infoModel.userName?.first()}"
                }
                dashboardFragmentBinding.userNameTv.text = infoModel.userName
                dashboardFragmentBinding.mobileNoTv.text = infoModel.mobile
            }
        }

        hotelAdapterInit()
        instituteAdapterInit()


        return dashboardFragmentBinding.root
    }

    private fun hotelAdapterInit(){
        var hotelList: ArrayList<HotelModel>? = HotelDataHelper.getAll(mActivity)
        if (hotelList != null && hotelList.size > 0) {
            hotelAdapter = HotelAdapter(hotelList, mActivity,mActivity.supportFragmentManager,activityDashboardBinding)
            dashboardFragmentBinding.hotelRv.layoutManager =
                LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            dashboardFragmentBinding.hotelRv.adapter = hotelAdapter
            dashboardFragmentBinding.noHotel.visibility = View.GONE
            dashboardFragmentBinding.hotelRv.visibility = View.VISIBLE
            dashboardFragmentBinding.viewAllHotel.visibility = View.VISIBLE
        }else{
            dashboardFragmentBinding.noHotel.visibility = View.VISIBLE
            dashboardFragmentBinding.hotelRv.visibility = View.GONE
            dashboardFragmentBinding.viewAllHotel.visibility = View.GONE
        }
    }

    private fun instituteAdapterInit(){

        var instituteList: ArrayList<InstituteModel>? = InstituteDataHelper.getAll(mActivity)
        if (instituteList != null && instituteList.size > 0) {
            instituteAdapter = InstituteAdapter(instituteList, mActivity,mActivity.supportFragmentManager,activityDashboardBinding)
            dashboardFragmentBinding.instituteRv.layoutManager =
                LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            dashboardFragmentBinding.instituteRv.adapter = instituteAdapter
            dashboardFragmentBinding.noInstitute.visibility = View.GONE
            dashboardFragmentBinding.instituteRv.visibility = View.VISIBLE
            dashboardFragmentBinding.viewAllInstitute.visibility = View.VISIBLE
        }else{
            dashboardFragmentBinding.noInstitute.visibility = View.VISIBLE
            dashboardFragmentBinding.instituteRv.visibility = View.GONE
            dashboardFragmentBinding.viewAllInstitute.visibility = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as DashboardActivity
    }

}