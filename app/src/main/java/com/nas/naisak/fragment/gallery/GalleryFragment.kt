package com.nas.naisak.fragment.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.PhotosRecyclerViewActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.ClassNameConstants.Companion.GALLERY
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.gallery.adapter.GalleryPhotosRecyclerviewAdapter
import com.nas.naisak.fragment.gallery.model.GetAlbumsRequestModel
import com.nas.naisak.fragment.gallery.model.GetAlbumsResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryFragment : Fragment() {
    var mTitleTextView: TextView? = null
    var moreImage: TextView? = null
//    var moreVideo:TextView? = null
    var photoImageIcon: ImageView? = null
//    var videoImageIcon:android.widget.ImageView? = null
    var viewGridPhotos: RecyclerView? = null
//    var viewGridVideo: RecyclerView? = null

    private var mRootView: View? = null
    private var mContext: Context? = null
    private val mAboutUsList: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var relMain: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    var mPhotosModelArrayList: ArrayList<GetAlbumsResponseModel.Data>? = null
//    var mVideoModelArrayList: ArrayList<PhotosListModel>? = null
//    var mPhotosRecyclerviewAdapter: GalleryPhotosRecyclerviewAdapter? = null
//    var mVideosRecyclerviewAdapter: GalleryVideoRecyclerviewAdapter? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
//    var recyclerViewLayoutManagerVideo: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_gallery, container,
            false
        )
        mContext = activity
        initialiseUI()
        return mRootView
    }


    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        moreImage = mRootView!!.findViewById<View>(R.id.moreImage) as TextView
//        moreVideo = mRootView.findViewById<View>(R.id.moreVideo) as TextView
//        videoImageIcon = mRootView.findViewById<View>(R.id.videoImageIcon) as ImageView
        photoImageIcon = mRootView!!.findViewById<View>(R.id.photoImageIcon) as ImageView
        viewGridPhotos = mRootView!!.findViewById<View>(R.id.viewGridPhotos) as RecyclerView
//        viewGridVideo = mRootView.findViewById<View>(R.id.viewGridVideo) as RecyclerView
        mTitleTextView!!.text = GALLERY
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener(View.OnClickListener { })
        viewGridPhotos!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 3)
//        recyclerViewLayoutManagerVideo = GridLayoutManager(mContext, 3)
        val spacing = 4 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext!!, spacing)
        viewGridPhotos!!.addItemDecoration(itemDecoration)
//        viewGridVideo.addItemDecoration(itemDecoration)
        viewGridPhotos!!.layoutManager = recyclerViewLayoutManager
//        viewGridVideo.setHasFixedSize(true)
//        viewGridVideo.setLayoutManager(recyclerViewLayoutManagerVideo)
        viewGridPhotos!!.addOnItemClickListener( object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(mContext, PhotosRecyclerViewActivity::class.java)
                intent.putExtra("photo_id", mPhotosModelArrayList!![position].id.toString())
                startActivity(intent)
            }

        })

        if (CommonMethods.isInternetAvailable(mContext!!)) {
            photosListApiCall()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext!!)
        }
        moreImage!!.setOnClickListener(View.OnClickListener {
            val mIntent = Intent(mContext, PhotosRecyclerViewActivity::class.java)
            mContext!!.startActivity(mIntent)
        })
        photoImageIcon!!.setOnClickListener(View.OnClickListener {
            val mIntent = Intent(mContext, PhotosRecyclerViewActivity::class.java)
            mContext!!.startActivity(mIntent)
        })
//        moreVideo.setOnClickListener(View.OnClickListener {
//            val mIntent = Intent(mContext, VideosRecyclerViewActivity::class.java)
//            mContext!!.startActivity(mIntent)
//        })
//        videoImageIcon.setOnClickListener(View.OnClickListener {
//            val mIntent = Intent(mContext, VideosRecyclerViewActivity::class.java)
//            mContext!!.startActivity(mIntent)
//        })
    }

    private fun photosListApiCall() {
        mPhotosModelArrayList = ArrayList()
        val body = GetAlbumsRequestModel("5","0")
        val token = PreferenceManager.getUserCode(mContext!!)
        val call: Call<GetAlbumsResponseModel> =
            ApiClient.getClient.getAlbums(body, "Bearer $token")
        call.enqueue(object : Callback<GetAlbumsResponseModel> {
            override fun onResponse(
                call: Call<GetAlbumsResponseModel>,
                response: Response<GetAlbumsResponseModel>
            ) {
                Log.e("Response", response.body()!!.message.toString())
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.size > 0 ){
                                Log.e("here", response.body()!!.message.toString())
                                for (i in response.body()!!.data!!.indices) {
                                    mPhotosModelArrayList!!.add(response.body()!!.data!![i]!!)
                                }
                                val mPhotosRecyclerviewAdapter = GalleryPhotosRecyclerviewAdapter (
                                    mContext!!, mPhotosModelArrayList!!
                                )
                                viewGridPhotos!!.adapter = mPhotosRecyclerviewAdapter
                            }else{
                                CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")

                            }
                        }else{
                            CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                        }
                    }else{
                        CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                    }
                }else{
                    CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<GetAlbumsResponseModel>, t: Throwable) {
                CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
            }

        })


    }

}