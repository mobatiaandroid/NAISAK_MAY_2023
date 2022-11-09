package com.nas.naisak.activity.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.TypedArray
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.nas.naisak.R
import com.nas.naisak.constants.PreferenceManager

class HomeListAdapter(
    private val context: Activity,
    private val title: Array<String>,
    private val imgid: TypedArray
) : ArrayAdapter<String>(context, R.layout.custom_homelist, title) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ViewHolder", "ResourceAsColor")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_homelist, null, true)
        val titleText = rowView.findViewById(R.id.listTxtView) as TextView
        val imageView = rowView.findViewById(R.id.listImg) as ImageView
        val badge = rowView.findViewById(R.id.badge) as TextView
        titleText.text = title[position]
        if (position==2)
        {
            if (PreferenceManager.getNotificationBadge(context)==0 && PreferenceManager.getNotificationEditedBadge(context)==0)
            {
                badge.visibility= View.GONE
            }
            else if (PreferenceManager.getNotificationBadge(context)==0 && PreferenceManager.getNotificationEditedBadge(context)>0)
            {
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getNotificationEditedBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_navy)
            }
            else if (PreferenceManager.getNotificationBadge(context)>0 && PreferenceManager.getNotificationEditedBadge(context)==0)
            {
                Log.e("It enters","notify")
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getNotificationBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_red)
            }
            else if (PreferenceManager.getNotificationBadge(context)>0 && PreferenceManager.getNotificationEditedBadge(context)>0)
            {
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getNotificationBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_red)
            }
            else
            {
                badge.visibility= View.GONE
            }
        }
        if (position==1)
        {
            if (PreferenceManager.getCalendarBadge(context)==0 && PreferenceManager.getCalendarEditedBadge(context)==0)
            {
                badge.visibility= View.GONE
            }
            else if (PreferenceManager.getCalendarBadge(context)==0 && PreferenceManager.getCalendarEditedBadge(context)>0)
            {
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getCalendarEditedBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_navy)

            }
            else if (PreferenceManager.getCalendarBadge(context)>0 && PreferenceManager.getCalendarEditedBadge(context)==0)
            {
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getCalendarBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_red)
            }
            else if (PreferenceManager.getCalendarBadge(context)>1 && PreferenceManager.getCalendarEditedBadge(context)>1)
            {
                badge.visibility= View.VISIBLE
                badge.text = PreferenceManager.getCalendarBadge(context).toString()
                badge.setBackgroundResource(R.drawable.shape_circle_red)
            }
            else{
                badge.visibility= View.GONE
            }
        }

        imageView.setImageResource(imgid.getResourceId(position, 0))
        return rowView
    }
}

