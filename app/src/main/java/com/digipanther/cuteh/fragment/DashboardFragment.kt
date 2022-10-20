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
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.AppBarHomeBinding
import com.digipanther.cuteh.databinding.FragmentDashboardBinding
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.model.HotelModel
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

        if(activityDashboardBinding != null) {
            activityDashboardBinding!!.appBarHome.contentMain.bottomNavigation.visibility =
                View.VISIBLE
            appbarMainBinding = activityDashboardBinding!!.appBarHome

            val toolbar = (mActivity as DashboardActivity?)!!.supportActionBar
            toolbar!!.title = "Dashboard"
            toolbar!!.subtitle = ""
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
        instituteAdapter = InstituteAdapter(mActivity)
        dashboardFragmentBinding.instituteRv.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        dashboardFragmentBinding.instituteRv.adapter = instituteAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as DashboardActivity
    }

}