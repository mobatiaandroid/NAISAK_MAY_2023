package com.nas.naisak.activity.cca.adapter

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
import kotlinx.android.synthetic.main.fragment_student_list_adapter.view.*

class StrudentSpinnerAdapter(mContext: Context, mStudentArray: ArrayList<StudentListReponseModel.Data.Lists>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext: Context? = mContext
    private val mStudentList: java.util.ArrayList<StudentListReponseModel.Data.Lists>? = mStudentArray
    var dept: String? = null


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var listTxtClass: TextView
        var imgIcon: ImageView

        init {
            mTitleTxt = view.findViewById<View>(R.id.listTxtTitle) as TextView
            listTxtClass = view.findViewById<View>(R.id.listTxtClass) as TextView
            imgIcon = view.findViewById<View>(R.id.imagicon) as ImageView
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_student_list_adapter, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.listTxtTitle.text = mStudentList!![position].name
        holder.itemView.imagicon.visibility = View.VISIBLE
        holder.itemView.listTxtClass.text = mStudentList!![position].student_class
        if (!mStudentList!![position].photo.equals("")) {
            Glide.with(mContext!!)
                .load(CommonMethods.replace(mStudentList!![position].photo.toString())).fitCenter()

                .placeholder(R.drawable.student).into(holder.itemView.imagicon)
        } else {
            holder.itemView.imagicon.setImageResource(R.drawable.student)
        }
    }

    override fun getItemCount(): Int {
        return mStudentList!!.size
    }

}
