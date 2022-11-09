package com.nas.naisak.fragment.notification.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.notification.AudioNotification
import com.nas.naisak.activity.notification.ImageNotificationActivity
import com.nas.naisak.activity.notification.TextNotificationActivity
import com.nas.naisak.activity.notification.VideoNotificationActivity
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.fragment.home.mContext
import com.nas.naisak.fragment.notification.model.NotificationListResponse

internal class NotificationListAdapter(
    private val context: Context,
    private var notificationList: List<NotificationListResponse>
) :
    RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var status: TextView = view.findViewById(R.id.status)
        var Img: ImageView = view.findViewById(R.id.Img)
        var statusLayout: RelativeLayout = view.findViewById(R.id.statusLayout)
        var mainRelative: RelativeLayout = view.findViewById(R.id.mainRelative)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_notification, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = notificationList[position]
        holder.title.text = list.title
        if (list.alertType.equals("Video"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_video)
        }
        else if (list.alertType.equals("Text"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_text)
        }
        else if (list.alertType.equals("Image"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_image)
        } else if (list.alertType.equals("Voice")) {
            holder.Img.setImageResource(R.drawable.alerticon_audio)
        }
        if (list.read_unread_status == 0) {
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.text = "new"
            holder.status.setBackgroundResource(R.drawable.rectangle_red)
        } else if (list.read_unread_status == 1) {
            holder.statusLayout.visibility = View.GONE
        } else if (list.read_unread_status == 2) {
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.text = "updated"
            holder.status.setBackgroundResource(R.drawable.rectangle_blue_update)
        }

        holder.mainRelative.setOnClickListener(View.OnClickListener {

            if (notificationList.get(position).read_unread_status == 0 || notificationList.get(
                    position
                ).read_unread_status == 2
            ) {
                notificationList.get(position).read_unread_status = 1
                PreferenceManager.setNotificationBadge(mContext, 0)
                PreferenceManager.setNotificationEditedBadge(mContext, 0)
                notifyDataSetChanged()
            }
            if (notificationList.get(position).alertType.equals("Text")) {
                val intent = Intent(context, TextNotificationActivity::class.java)
                intent.putExtra("id", notificationList.get(position).id.toString())
                intent.putExtra("title", notificationList.get(position).title)
                context.startActivity(intent)
            } else if (notificationList.get(position).alertType.equals("Image")) {
                val intent = Intent(context, ImageNotificationActivity::class.java)
                intent.putExtra("id", notificationList.get(position).id.toString())
                intent.putExtra("title", notificationList.get(position).title)
                context.startActivity(intent)
            } else if (notificationList.get(position).alertType.equals("Video")) {
                val intent = Intent(context, VideoNotificationActivity::class.java)
                intent.putExtra("id", notificationList.get(position).id.toString())
                intent.putExtra("title", notificationList.get(position).title)
                context.startActivity(intent)
            } else if (notificationList.get(position).alertType.equals("Voice")) {
                val intent = Intent(context, AudioNotification::class.java)
                intent.putExtra("id", notificationList.get(position).id.toString())
                intent.putExtra("title", notificationList.get(position).title)
                context.startActivity(intent)
            }
        })


    }
    override fun getItemCount(): Int {
        return notificationList.size
    }
}