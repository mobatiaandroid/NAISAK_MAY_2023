package com.nas.naisak.activity.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.model.GetPhotoResponseModel
import com.nas.naisak.constants.CommonMethods
import kotlinx.android.synthetic.main.photos_view_recyclerview_adapter.view.*

class PhotosViewRecyclerviewAdapter(mContext: Context, mPhotosModelArrayList: ArrayList<GetPhotoResponseModel.Data>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext: Context? = mContext
    private val mPhotosModelArrayList: java.util.ArrayList<GetPhotoResponseModel.Data>? = mPhotosModelArrayList
    var photo_id = "-1"

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photoImageView: ImageView

        //        TextView photoDescription;
        //        TextView photoTitle;
        var gridClickRelative: RelativeLayout

        init {
            photoImageView = view.findViewById<View>(R.id.imgView) as ImageView
            gridClickRelative = view.findViewById<View>(R.id.gridClickRelative) as RelativeLayout
            //            photoDescription = (TextView) view.findViewById(R.id.photoDescription);
//            photoTitle = (TextView) view.findViewById(R.id.photoTitle);
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.photos_view_recyclerview_adapter,
                parent,
                false
            ) //photos_recyclerview_adapter


        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //        holder.photoTitle.setText(mPhotosModelArrayList.get(position).getTitle());
//        holder.photoDescription.setText(mPhotosModelArrayList.get(position).getDescription());
        if (!mPhotosModelArrayList!![position].image.equals("",ignoreCase = true)) {
            Glide.with(mContext!!)
                .load(CommonMethods.replace(mPhotosModelArrayList[position].image.toString()))
                .centerCrop().fitCenter()

                .into(holder.itemView.imgView)
        }
        if (!photo_id.equals("-1", ignoreCase = true)) {
            if (photo_id.equals(mPhotosModelArrayList[position].id.toString(), ignoreCase = true)) {
//                didTapButton(holder.photoImageView);
                holder.itemView.gridClickRelative.setBackgroundResource(R.color.red)
            }
        }
    }

    override fun getItemCount(): Int {
        return mPhotosModelArrayList!!.size
    }

}
