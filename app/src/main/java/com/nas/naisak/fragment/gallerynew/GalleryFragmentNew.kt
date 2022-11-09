package com.nas.naisak.fragment.gallerynew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.PhotosViewRecyclerViewActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.ClassNameConstants
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.gallerynew.adapter.AlbumRecyclerviewAdapter
import com.nas.naisak.fragment.gallerynew.model.GetAlbumDataModel
import com.nas.naisak.fragment.gallerynew.model.GetAlbumResponseModelnew
import com.nas.naisak.fragment.gallerynew.model.GetAlbumsRequestModelNew
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryFragmentNew : Fragment() {
    var mTitleTextView: TextView? = null
    private var mRootView: View? = null
    lateinit var mContext: Context

    lateinit var recycler_view_photos:RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var photoArrayList:ArrayList<GetAlbumDataModel>

     var start:Int=0
     var limit:Int=20


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        mRootView = inflater.inflate(
            R.layout.fragment_gallery_new, container,
            false
        )
        mContext = requireActivity()
        initialiseUI()
        start=0
        limit=20
        photosListApiCall(limit.toString(),start.toString())
        return mRootView
    }


    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView!!.text = ClassNameConstants.GALLERY


        recycler_view_photos=mRootView!!.findViewById(R.id.recycler_view_photos)
        linearLayoutManager = LinearLayoutManager(mContext)
        recycler_view_photos.layoutManager = linearLayoutManager

        recycler_view_photos!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(mContext, PhotosViewRecyclerViewActivity::class.java)
                //                            intent.putExtra("photo_array", mPhotosModelArrayList);
//                        AppController.mPhotosModelArrayListGallery=mPhotosModelArrayList;
                intent.putExtra("albumID", photoArrayList.get(position).id.toString())
                intent.putExtra("pos", position)
                startActivity(intent)
            }

        })



    }




    private fun photosListApiCall(start: String, limit: String) {
        photoArrayList= ArrayList()
        val body = GetAlbumsRequestModelNew("5","0")
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<GetAlbumResponseModelnew> =
            ApiClient.getClient.getAlbumsNew(body, "Bearer $token")
        call.enqueue(object : Callback<GetAlbumResponseModelnew> {
            override fun onResponse(
                call: Call<GetAlbumResponseModelnew>,
                response: Response<GetAlbumResponseModelnew>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status==100){
                            if ( response.body()!!.dataArrayList.size>0){

                                photoArrayList.addAll(response.body()!!.dataArrayList)

                              var  mPhotosRecyclerviewAdapter = AlbumRecyclerviewAdapter(mContext, photoArrayList!!)
                                recycler_view_photos!!.adapter = mPhotosRecyclerviewAdapter
                            }else{

                                Toast.makeText(mContext, "Gallery is empty", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                        }
                    }else{
                        CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                    }
                }else{
                    CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<GetAlbumResponseModelnew>, t: Throwable) {
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })


    }

}