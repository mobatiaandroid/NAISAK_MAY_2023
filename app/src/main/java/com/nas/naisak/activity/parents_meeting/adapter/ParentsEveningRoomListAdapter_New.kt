package com.nas.naisak.activity.parents_meeting.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.calender.model.CalendarEventsModel
import kotlinx.android.synthetic.main.fragment_room_list_adapter.view.*

class ParentsEveningRoomListAdapter_New(mContext: Context, mListViewArray: ArrayList<CalendarEventsModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mSocialMediaModels: java.util.ArrayList<CalendarEventsModel>? = null
    var mContext: Context? = null
    var iconImage: Drawable? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgIcon: ImageView
        var listTxtView: TextView
        var listTxtClass: TextView

        init {
            imgIcon = view.findViewById<View>(R.id.imagicon) as ImageView
            listTxtView = view.findViewById<View>(R.id.listTxtTitle) as TextView
            listTxtClass = view.findViewById<View>(R.id.listTxtClass) as TextView
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_room_list_adapter, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.listTxtTitle.text = mSocialMediaModels!![position].startTime.toString() + " - " + mSocialMediaModels!![position].endTime
        holder.itemView.listTxtClass.text = "Room:" + mSocialMediaModels!![position].room
    }

    override fun getItemCount(): Int {
        return mSocialMediaModels!!.size;
    }

}
