package com.nas.naisak.activity.parents_meeting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.calender.model.CalendarEventsModel
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import kotlinx.android.synthetic.main.time_slot_parents_evening_adapter.view.*

class ParentsEveningTimeSlotRecyclerviewAdapter(mContext: Context, mListViewArray: ArrayList<CalendarEventsModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext: Context? = mContext
    private val mCalendarEventsModelArrayList: java.util.ArrayList<CalendarEventsModel>? = mListViewArray
    var photo_id = "-1"
    var startTime = ""
    var startTimeAm = true
    var endTimeAm = true
    var endTime = ""

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photoImageView: ImageView? = null
        var timeTo: TextView
        var timeFrom: TextView
        var textViewTo: TextView
        var gridClickRelative: LinearLayout
        var card_view: CardView

        init {

//            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            timeTo = view.findViewById<View>(R.id.timeTo) as TextView
            timeFrom = view.findViewById<View>(R.id.timeFrom) as TextView
            textViewTo = view.findViewById<View>(R.id.textViewTo) as TextView
            gridClickRelative = view.findViewById<View>(R.id.gridClickRelative) as LinearLayout
            card_view = view.findViewById<View>(R.id.card_view) as CardView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_slot_parents_evening_adapter, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mCalendarEventsModelArrayList!![position].startTime.contains("am")) {
            startTime =
                CommonMethods.replaceam(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("pm")) {
            startTime =
                CommonMethods.replacepm(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("AM")) {
            startTime =
                CommonMethods.replaceAM(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("PM")) {
            startTime =
                CommonMethods.replacePM(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("A.M.")) {
            startTime =
                CommonMethods.replaceAMDot(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("P.M.")) {
            startTime =
                CommonMethods.replacePMDot(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("a.m.")) {
            startTime =
                CommonMethods.replaceamdot(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].startTime.contains("p.m.")) {
            startTime =
                CommonMethods.replacepmdot(mCalendarEventsModelArrayList[position].startTime)!!.trim()
            startTimeAm = false
        }

        //===starttime

        //===starttime
        if (mCalendarEventsModelArrayList[position].endTime.contains("am")) {
            endTime = CommonMethods.replaceam(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("pm")) {
            endTime = CommonMethods.replacepm(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("AM")) {
            endTime = CommonMethods.replaceAM(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("PM")) {
            endTime = CommonMethods.replacePM(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("A.M.")) {
            endTime =
                CommonMethods.replaceAMDot(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("P.M.")) {
            endTime =
                CommonMethods.replacePMDot(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = false
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("a.m.")) {
            endTime =
                CommonMethods.replaceamdot(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = true
        } else if (mCalendarEventsModelArrayList[position].endTime.contains("p.m.")) {
            endTime =
                CommonMethods.replacepmdot(mCalendarEventsModelArrayList[position].endTime)!!.trim()
            endTimeAm = false
        }

        if (mCalendarEventsModelArrayList[position].status.equals("0")) {
            holder.itemView.gridClickRelative.setBackgroundResource(R.drawable.time_curved_rel_layout)
            //            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));
            holder.itemView.textViewTo.setTextColor(mContext!!.resources.getColor(R.color.black))
            holder.itemView.timeTo.setTextColor(mContext.resources.getColor(R.color.black))
            holder.itemView.timeFrom.setTextColor(mContext.resources.getColor(R.color.black))
        } else if (mCalendarEventsModelArrayList[position].status.equals("1")) {
            holder.itemView.gridClickRelative.setBackgroundResource(R.drawable.slotbooked_curved_rel_layout)
            //            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));
            holder.itemView.textViewTo.setTextColor(mContext!!.resources.getColor(R.color.black))
            holder.itemView.timeTo.setTextColor(mContext.resources.getColor(R.color.black))
            holder.itemView.timeFrom.setTextColor(mContext.resources.getColor(R.color.black))
        } else if (mCalendarEventsModelArrayList[position].status.equals("2")) {
            holder.itemView.gridClickRelative.setBackgroundResource(R.drawable.slotbookedbyuser_curved_rel_layout)
            //            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.itemView.textViewTo.setTextColor(mContext!!.resources.getColor(R.color.black))
            holder.itemView.timeTo.setTextColor(mContext.resources.getColor(R.color.black))
            holder.itemView.timeFrom.setTextColor(mContext.resources.getColor(R.color.black))
        } else if (mCalendarEventsModelArrayList[position].status.equals("3")) {
            PreferenceManager.setPTABooked(mContext,true)
            PreferenceManager.setPTABookedID(mContext,
                mCalendarEventsModelArrayList[position].id.toString()
            )
            holder.itemView.gridClickRelative.setBackgroundResource(R.drawable.parent_slot_new)
            holder.itemView.textViewTo.setTextColor(mContext!!.resources.getColor(R.color.white))
            holder.itemView.timeTo.setTextColor(mContext.resources.getColor(R.color.white))
            holder.itemView.timeFrom.setTextColor(mContext.resources.getColor(R.color.white))
        }


        if (startTimeAm) {
            holder.itemView.timeFrom.text = "$startTime\nAM"
        } else {
            holder.itemView.timeFrom.text = "$startTime\nPM"
        }
        if (endTimeAm) {
            holder.itemView.timeTo.text = "$endTime\nAM"
        } else {
            holder.itemView.timeTo.text = "$endTime\nPM"
        }
    }

    override fun getItemCount(): Int {
        return mCalendarEventsModelArrayList!!.size
    }

}
