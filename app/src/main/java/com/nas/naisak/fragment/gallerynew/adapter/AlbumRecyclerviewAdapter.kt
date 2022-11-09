package com.nas.naisak.fragment.gallerynew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.fragment.gallerynew.model.GetAlbumDataModel
import kotlinx.android.synthetic.main.photos_recyclerview_adapter.view.*

class AlbumRecyclerviewAdapter(mContext: Context, mPhotosModelArrayList: ArrayList<GetAlbumDataModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val mContext: Context = mContext
    private val mPhotosModelArrayList: java.util.ArrayList<GetAlbumDataModel> = mPhotosModelArrayList
    var photo_id = "-1"
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photoImageView: ImageView
        var photoTitle: TextView
        var gridClickRelative: RelativeLayout

        init {
            photoImageView = view.findViewById<View>(R.id.imgView) as ImageView
            gridClickRelative = view.findViewById<View>(R.id.gridClickRelative) as RelativeLayout
            photoTitle = view.findViewById<View>(R.id.photoTitle) as TextView
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.photos_recyclerview_adapter,
                parent,
                false
            ) //photos_recyclerview_adapter


        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.photoTitle.setText(mPhotosModelArrayList!![position].title)
        if (!mPhotosModelArrayList[position].cover_image.toString().equals("",ignoreCase = true)) {
            Glide.with(mContext!!)
                .load(CommonMethods.replace(mPhotosModelArrayList[position].cover_image.toString())).fitCenter()

                .into(holder.itemView.imgView)
        }
    }

    override fun getItemCount(): Int {
        return mPhotosModelArrayList!!.size
    }

}