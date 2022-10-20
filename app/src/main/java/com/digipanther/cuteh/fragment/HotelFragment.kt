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
import com.digipanther.cuteh.adapter.HotelAdapter
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.databinding.FragmentHotelBinding
import com.digipanther.cuteh.dbHelper.HotelDataHelper
import com.digipanther.cuteh.model.HotelModel
import kotlinx.android.synthetic.main.layout_toolbar_white_bg.view.*

class HotelFragment : Fragment,View.OnClickListener{

    private lateinit var hotelBinding: FragmentHotelBinding
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private var mActivity = FragmentActivity()
    private lateinit var hotelAdapter : HotelAdapter

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
        hotelBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hotel, container, false)

        return hotelBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardBinding.appBarHome.toolbar.visibility = View.GONE
        hotelBinding.whiteToolbar.img_back.setOnClickListener(this)
        hotelBinding.cardAddHotel.setOnClickListener(this)
        showHotelList()
    }

    private fun showHotelList() {
        var hotelList: ArrayList<HotelModel>? = HotelDataHelper.getAll(mActivity)

        if (hotelList != null && hotelList.size > 0) {
            hotelBinding.hotelRv.visibility = View.VISIBLE
            hotelBinding.llNoData.visibility = View.GONE

            hotelAdapter = HotelAdapter(hotelList,mActivity,mActivity.supportFragmentManager,dashboardBinding)
            hotelBinding.hotelRv.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
            hotelBinding.hotelRv.adapter = hotelAdapter

        } else {
            hotelBinding.hotelRv.visibility = View.GONE
            hotelBinding.llNoData.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.cardAddHotel -> {
                Utility.replaceFragment(HotelAddEditFragment(),mActivity.supportFragmentManager,R.id.layout_fragment)
            }

            R.id.img_back -> {
                mActivity.onBackPressed()
            }
        }
    }


}