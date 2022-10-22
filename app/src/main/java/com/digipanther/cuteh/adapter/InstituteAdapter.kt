package com.digipanther.cuteh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.digipanther.cuteh.R
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityDashboardBinding
import com.digipanther.cuteh.fragment.InstituteDetailsFragment
import com.digipanther.cuteh.model.InstituteModel

class InstituteAdapter : RecyclerView.Adapter<InstituteAdapter.ViewHolder> {

    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    var context: Context? = null
    private lateinit var instituteList: List<InstituteModel>
    var emptyVal: String? = "N/A"
    private lateinit var fragmentManager: FragmentManager

    constructor()

    constructor(context: Context) {
        this.context = context
    }


    constructor(instituteList: List<InstituteModel>, context: Context) {
        this.context = context
        this.instituteList = instituteList
    }

    constructor(
        instituteList: List<InstituteModel>,
        context: Context,
        fragmentManager: FragmentManager,
        activityDashboardBinding: ActivityDashboardBinding,
    ) {
        this.context = context
        this.instituteList = instituteList
        this.fragmentManager = fragmentManager
        this.activityDashboardBinding = activityDashboardBinding
    }

    constructor(
        instituteList: List<InstituteModel>,
        context: Context,
        activityDashboardBinding: ActivityDashboardBinding,
    ) {
        this.context = context
        this.instituteList = instituteList
        this.activityDashboardBinding = activityDashboardBinding
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_institute, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var instituteModel: InstituteModel = instituteList[position]


        if (!Utility.isNullOrEmpty(instituteModel.COLLEGE_NAME)) {
            holder.instituteNameTv.text = instituteModel.COLLEGE_NAME
        } else {
            holder.instituteNameTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(instituteModel.MOBILE_NO)) {
            holder.instituteMobileTv.text = instituteModel.MOBILE_NO
        } else {
            holder.instituteMobileTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(instituteModel.BRANCH)) {
            holder.instituteAddressTv.text = instituteModel.BRANCH
        } else {
            holder.instituteAddressTv.text = emptyVal
        }

        if (!Utility.isNullOrEmpty(instituteModel.COURSE)) {
            holder.courseNameTv.text = instituteModel.COURSE
            holder.courseLay.visibility = View.VISIBLE
        } else {
            holder.courseNameTv.text = emptyVal
            holder.courseLay.visibility = View.GONE
        }

        holder.courseFeeTv.text = "Rs. ${instituteModel.TUITION_FEES}"


        holder.itemView.setOnClickListener(View.OnClickListener {
            Utility.replaceFragment(InstituteDetailsFragment(instituteModel,
                activityDashboardBinding),
                fragmentManager,
                R.id.layout_fragment)
        })
    }

    override fun getItemCount(): Int {
        return instituteList.size;
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instituteNameTv: TextView = itemView.findViewById(R.id.instituteNameTv);
        val instituteMobileTv: TextView = itemView.findViewById(R.id.instituteMobileTv);
        val instituteAddressTv: TextView = itemView.findViewById(R.id.instituteAddressTv);
        val courseFeeTv: TextView = itemView.findViewById(R.id.courseFeeTv);
        val courseNameTv: TextView = itemView.findViewById(R.id.courseNameTv);
        val backgroundRL: RelativeLayout = itemView.findViewById(R.id.backgroundRL);
        val courseLay: LinearLayout = itemView.findViewById(R.id.courseLay);
    }
}