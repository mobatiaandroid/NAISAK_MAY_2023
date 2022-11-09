package com.nas.naisak.fragment.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.fragment.gallery.model.GetAlbumsResponseModel
import kotlinx.android.synthetic.main.gallery_thumbnail_recyclerview_adapter.view.*

class GalleryPhotosRecyclerviewAdapter(mContext: Context, mPhotosModelArrayList: ArrayList<GetAlbumsResponseModel.Data>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var context = mContext
    var photosArray: java.util.ArrayList<GetAlbumsResponseModel.Data>? = mPhotosModelArrayList

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photoImageView: ImageView

        init {
            photoImageView = view.findViewById<View>(R.id.imgView) as ImageView
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_thumbnail_recyclerview_adapter, parent, false)

        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
        if (!photosArray!![position].cover_image.equals("")) {
            Glide.with(context)
                .load(CommonMethods.replace(photosArray!![position].cover_image.toString()))
                .into(holder.itemView.imgView)
        }
    }

    override fun getItemCount(): Int {
        return photosArray!!.size
    }

}
