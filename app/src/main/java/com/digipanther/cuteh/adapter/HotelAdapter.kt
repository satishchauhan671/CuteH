package com.digipanther.cuteh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.digipanther.cuteh.R
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.fragment.HotelAddEditFragment
import com.digipanther.cuteh.fragment.HotelDetailsFragment
import com.digipanther.cuteh.model.HotelModel

class HotelAdapter : RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    var context: Context? = null
    private lateinit var hotelList: List<HotelModel>
    var emptyVal : String? = "N/A"
    private lateinit var fragmentManager : FragmentManager

    constructor()

    constructor(hotelList: List<HotelModel>, context: Context) {
        this.context = context
        this.hotelList = hotelList
    }

    constructor(hotelList: List<HotelModel>, context: Context, fragmentManager: FragmentManager, activityDashboardBinding: ActivityDashboardBinding) {
        this.context = context
        this.hotelList = hotelList
        this.fragmentManager = fragmentManager
        this.activityDashboardBinding = activityDashboardBinding
    }

    constructor(hotelList: List<HotelModel>, context: Context, activityDashboardBinding: ActivityDashboardBinding) {
        this.context = context
        this.hotelList = hotelList
        this.activityDashboardBinding = activityDashboardBinding
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_hotel, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var hotelModel : HotelModel = hotelList[position]

        if (!Utility.isNullOrEmpty(hotelModel.BUSINESS_NAME)){
            holder.hotelNameTv.text = hotelModel.BUSINESS_NAME
        }else{
            holder.hotelNameTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(hotelModel.MOBILE_NO)){
            holder.hotelMobileTv.text = hotelModel.MOBILE_NO
        }else{
            holder.hotelMobileTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(hotelModel.ADDRESS)){
            holder.hotelAddressTv.text = hotelModel.ADDRESS
        }else{
            holder.hotelAddressTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(hotelModel.BOOK_NOW)){
            holder.bookNowTv.text = "Book Now"
        }else{
            holder.bookNowTv.visibility = View.GONE
        }

        if (!Utility.isNullOrEmpty(hotelModel.RATE)){
            holder.rateTv.text = hotelModel.RATE
        }else{
            holder.rateTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(hotelModel.PRICE)){
            holder.priceTv.text = "Rs. ${hotelModel.PRICE}"
        }else{
            holder.priceTv.text = emptyVal
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            Utility.replaceFragment(HotelDetailsFragment(hotelModel, activityDashboardBinding),
                fragmentManager,
                R.id.layout_fragment)
        })
    }

    override fun getItemCount(): Int {
        return hotelList.size;
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelNameTv: TextView = itemView.findViewById(R.id.hotelNameTv);
        val hotelMobileTv: TextView = itemView.findViewById(R.id.hotelMobileTv);
        val hotelAddressTv: TextView = itemView.findViewById(R.id.hotelAddressTv);
        val bookNowTv: TextView = itemView.findViewById(R.id.bookNowTv);
        val rateTv: TextView = itemView.findViewById(R.id.rateTv);
        val priceTv: TextView = itemView.findViewById(R.id.priceTv);
        val backgroundRL: RelativeLayout = itemView.findViewById(R.id.backgroundRL);
    }
}