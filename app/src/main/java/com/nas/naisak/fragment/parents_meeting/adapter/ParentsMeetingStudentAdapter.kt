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
import com.nas.naisak.commonmodels.StudentListReponseModel
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.fragment.home.mContext
import kotlinx.android.synthetic.main.student_list_adapter.view.*

class ParentsMeetingStudentAdapter(mContext: Context, mListViewArray: ArrayList<StudentListReponseModel.Data.Lists>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context = mContext
    var studentListArray: java.util.ArrayList<StudentListReponseModel.Data.Lists>? = mListViewArray
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgIcon: ImageView = view.findViewById(R.id.imagicon)
        var listTxtView: TextView = view.findViewById(R.id.listTxtTitle)
        var listTxtClass: TextView = view.findViewById(R.id.listTxtClass)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_adapter, parent, false)

        return ParentsMeetingStaffAdapter.MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


//   holder.imgIcon.setVisibility(View.GONE);
//        holder.imgIcon.setBackgroundResource(R.drawable.roundfb);
        if (!studentListArray!![position].photo.equals("")) {
            Glide.with(mContext)
                .load(CommonMethods.replace(studentListArray!!.get(position).photo.toString()))
                .placeholder(R.drawable.student).error(R.drawable.student).fitCenter()
                .into(holder.itemView.imagicon)
        } else {
            holder.itemView.imagicon.setImageResource(R.drawable.student)
        }
        holder.itemView.listTxtTitle.setText(studentListArray!![position].name)
        holder.itemView.listTxtClass.setText(studentListArray!!.get(position).student_class)
    }

    override fun getItemCount(): Int {
        return studentListArray!!.size
    }

}
