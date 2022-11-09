package com.nas.naisak.fragment.calendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.fragment.calendar.model.CalendarDetailsModelUse

class CalendarDetailListAdapter(
    private var mContext: Context,
    private var calendarModels: List<CalendarDetailsModelUse>,
    private var colors: Int,
    private var mPosition: Int,
    private var isRead: Boolean
) : RecyclerView.Adapter<CalendarDetailListAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var eventName: TextView = view.findViewById(R.id.eventName)
        var eventTime: TextView = view.findViewById(R.id.eventTime)
        var addicon: ImageView = view.findViewById(R.id.addicon)
        var removeicon: ImageView = view.findViewById(R.id.removeicon)
        var statusImg: ImageView = view.findViewById(R.id.statusImg)



    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calendar_detail, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.e("ADAPTAR", "DETAIL")
        holder.eventName.text = calendarModels.get(position).title

        if (calendarModels.get(position).isAllday.equals("1")) {
            holder.eventTime.text = "All Day Event"
        } else {
            if (!calendarModels.get(position).starttime.equals("") && !(calendarModels.get(position).endtime).equals(
                    ""
                )
            ) {
                holder.eventTime.text =
                    calendarModels.get(position).starttime + " - " + calendarModels.get(
                        position
                    ).endtime
            } else if (!calendarModels.get(position).starttime.equals("") && (calendarModels.get(
                    position
                ).endtime).equals("")
            ) {
                holder.eventTime.text = calendarModels.get(position).starttime
            } else if (calendarModels.get(position).starttime.equals("") && !(calendarModels.get(
                    position
                ).endtime).equals("")
            ) {
                holder.eventTime.text = calendarModels.get(position).endtime
            }
        }



        holder.eventTime.setTextColor(colors)
        holder.eventName.setTextColor(colors)

        if (colors == mContext.resources.getColor(R.color.cal_row_1)) {
            holder.addicon.setImageResource(R.drawable.addicon4)
            holder.removeicon.setImageResource(R.drawable.minimize4)
        } else if (colors == mContext.resources.getColor(R.color.cal_row_2)) {
            holder.addicon.setImageResource(R.drawable.addicon3)
            holder.removeicon.setImageResource(R.drawable.minimize3)
        } else if (colors == mContext.resources.getColor(R.color.cal_row_3)) {
            holder.addicon.setImageResource(R.drawable.addicon2)
            holder.removeicon.setImageResource(R.drawable.minimize2)
        } else if (colors == mContext.resources.getColor(R.color.cal_row_4)) {
            holder.addicon.setImageResource(R.drawable.addicon1)
            holder.removeicon.setImageResource(R.drawable.minimize1)
        }

        if (calendarModels.get(position).status.equals("0")) {
            holder.statusImg.visibility = View.VISIBLE

            holder.statusImg.setImageResource(R.drawable.shape_circle_red)
        } else if (calendarModels.get(position).status.equals("2")) {
            holder.statusImg.visibility = View.VISIBLE

            holder.statusImg.setImageResource(R.drawable.shape_circle_navy)

        } else if (calendarModels.get(position).status.equals("1") || calendarModels.get(position).status.equals(
                ""
            )
        ) {
            holder.statusImg.visibility = View.INVISIBLE
        }

    }
    override fun getItemCount(): Int {
        Log.e("SIZE ADA", calendarModels.size.toString())
        return calendarModels.size

    }

}