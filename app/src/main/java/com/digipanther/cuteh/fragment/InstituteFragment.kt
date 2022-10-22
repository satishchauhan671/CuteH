package com.digipanther.cuteh.fragment

import android.content.Context
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
import com.digipanther.cuteh.adapter.InstituteAdapter
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.FragmentInstituteBinding
import com.digipanther.cuteh.dbHelper.InstituteDataHelper
import com.digipanther.cuteh.model.InstituteModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*

class InstituteFragment : Fragment,View.OnClickListener{

    private lateinit var instituteBinding: FragmentInstituteBinding
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private var mActivity = FragmentActivity()
    private lateinit var instituteAdapter : InstituteAdapter

    constructor() : super()

    constructor(dashboardBinding: ActivityDashboardBinding){
        this.dashboardBinding = dashboardBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        instituteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_institute, container, false)

        return instituteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardBinding.appBarHome.toolbar.visibility = View.GONE
        instituteBinding.whiteToolbar.img_back.setOnClickListener(this)
        instituteBinding.cardAddInstitute.setOnClickListener(this)
        showInstituteList()
    }

    private fun showInstituteList() {
        var instituteList: ArrayList<InstituteModel>? = InstituteDataHelper.getAll(mActivity)

        if (instituteList != null && instituteList.size > 0) {
            instituteBinding.hotelRv.visibility = View.VISIBLE
            instituteBinding.llNoData.visibility = View.GONE

            instituteAdapter = InstituteAdapter(instituteList,mActivity,mActivity.supportFragmentManager,dashboardBinding)
            instituteBinding.hotelRv.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
            instituteBinding.hotelRv.adapter = instituteAdapter

        } else {
            instituteBinding.hotelRv.visibility = View.GONE
            instituteBinding.llNoData.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.cardAddInstitute -> {
                Utility.replaceFragment(InstituteAddEditFragment(dashboardBinding),mActivity.supportFragmentManager,R.id.layout_fragment)
            }

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }


}