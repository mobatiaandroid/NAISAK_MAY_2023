package com.nas.naisak.fragment.parents_meeting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.commonmodels.StaffListByStudentResponseModel
import com.nas.naisak.constants.CommonMethods
import kotlinx.android.synthetic.main.staff_list_adapter.view.*

class ParentsMeetingStaffAdapter(mContext: Context, mListViewStaffArray: ArrayList<StaffListByStudentResponseModel.Data.Lists>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context = mContext
    var staffListArray: java.util.ArrayList<StaffListByStudentResponseModel.Data.Lists>? = mListViewStaffArray
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgIcon: ImageView = view.findViewById(R.id.imagicon)
        var listTxtView: TextView = view.findViewById(R.id.listTxtTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.staff_list_adapter, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


//   holder.imgIcon.setVisibility(View.GONE);
//   holder.imgIcon.setBackgroundResource(R.drawable.roundfb);
        holder.itemView.listTxtTitle.text = staffListArray!![position].name
        if (!(staffListArray!![position].image_url).equals("")){
            Glide.with(context)
                .load(CommonMethods.replace(staffListArray!![position].image_url.toString()))
                .placeholder(R.drawable.staff).error(R.drawable.staff).fitCenter()
                .into(holder.itemView.imagicon)
        }else {
            holder.itemView.imagicon.setImageResource(R.drawable.staff)
        }


    }

    override fun getItemCount(): Int {
        return staffListArray!!.size
    }

}
