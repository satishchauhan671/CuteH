package com.digipanther.cuteh.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.digipanther.cuteh.databinding.FragmentHotelBinding

class HotelFragment : Fragment {

    private lateinit var hotelBinding: FragmentHotelBinding

    constructor() : super()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        hotelBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hotel,container,false)


        return hotelBinding.root
    }

    private var mActivity = FragmentActivity()

}