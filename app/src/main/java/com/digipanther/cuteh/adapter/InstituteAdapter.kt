package com.digipanther.cuteh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digipanther.cuteh.R

class InstituteAdapter : RecyclerView.Adapter<InstituteAdapter.ViewHolder> {

    var context: Context? = null

    constructor()

    constructor(context: Context) {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_institute, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 2;
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instituteNameTv: TextView = itemView.findViewById(R.id.instituteNameTv);
        val instituteMobileTv: TextView = itemView.findViewById(R.id.instituteMobileTv);
        val instituteAddressTv: TextView = itemView.findViewById(R.id.instituteAddressTv);
        val courseFeeTv: TextView = itemView.findViewById(R.id.courseFeeTv);
        val courseNameTv: TextView = itemView.findViewById(R.id.courseNameTv);
        val backgroundRL: RelativeLayout = itemView.findViewById(R.id.backgroundRL);
    }
}