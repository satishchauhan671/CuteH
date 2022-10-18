package com.digipanther.cuteh.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.DashboardActivity
import com.digipanther.cuteh.databinding.ActivityMainBinding
import com.digipanther.cuteh.databinding.AppBarHomeBinding
import com.digipanther.cuteh.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment {
    private var activityDashboardBinding : ActivityMainBinding? = null
    private lateinit var dashboardFragmentBinding: FragmentDashboardBinding
    private lateinit var appbarMainBinding: AppBarHomeBinding
    private var mActivity = FragmentActivity()

    constructor() : super()

    constructor(activityDashboardBinding: ActivityMainBinding){
        this.activityDashboardBinding = activityDashboardBinding
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        super.onCreateView(inflater, container, savedInstanceState)
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

        return dashboardFragmentBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as DashboardActivity
    }

}