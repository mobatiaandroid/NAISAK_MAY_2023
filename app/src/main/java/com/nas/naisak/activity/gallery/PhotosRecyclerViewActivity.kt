package com.nas.naisak.activity.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.adapter.PhotosRecyclerviewAdapter
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.gallery.model.GetAlbumsRequestModel
import com.nas.naisak.fragment.gallery.model.GetAlbumsResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotosRecyclerViewActivity : AppCompatActivity() {
    var mContext: Context = this
    private var recycler_view_photos: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    lateinit var progressBar: ProgressBar
//    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
//    var intent: Intent? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var mPhotosModelArrayList: ArrayList<GetAlbumsResponseModel.Data>? = null
//    var mPhotosModelUrlArrayList: ArrayList<PhotosListModel>? = null
    var mPhotosRecyclerviewAdapter: PhotosRecyclerviewAdapter? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    var extras: Bundle? = null
    var photo_id = "-1"
    var scrollTo = ""
    var apiID = ""
    var isFromBottom = false
    var notificationSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_recycler_view)
        mContext = this
        mPhotosModelArrayList = java.util.ArrayList<GetAlbumsResponseModel.Data>()
//        AppController.mPhotosModelArrayListGallery = java.util.ArrayList<PhotosListModel>()
        extras = intent.extras

        if (extras != null) {
            photo_id = extras!!.getString("photo_id")!!
        }
        initilaiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            scrollTo = ""
            apiID = ""
            isFromBottom = false
            photosListApiCall(apiID, scrollTo)
        } else {
           CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun photosListApiCall(start: String, limit: String) {
        val body = GetAlbumsRequestModel("5","0")
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<GetAlbumsResponseModel> =
            ApiClient.getClient.getAlbums(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<GetAlbumsResponseModel> {
            override fun onResponse(
                call: Call<GetAlbumsResponseModel>,
                response: Response<GetAlbumsResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.size > 0 ){
                                for (i in response.body()!!.data!!.indices){
                                    mPhotosModelArrayList!!.add(response.body()!!.data!![i]!!)
                                }
                                if (mPhotosModelArrayList!!.size > 0) {
                                    mPhotosRecyclerviewAdapter = PhotosRecyclerviewAdapter(
                                        mContext,
                                        mPhotosModelArrayList!!,
                                        photo_id
                                    )
                                    recycler_view_photos!!.adapter = mPhotosRecyclerviewAdapter
//                                    if (isFromBottom) {
//                                        recycler_view_photos!!.scrollToPosition(
//                                            mPhotosModelArrayList!!.size - notificationSize - 2
//                                        )
//                                    } else {
//                                        recycler_view_photos!!.scrollToPosition(0)
//                                    }
//                                    mPhotosRecyclerviewAdapter.setOnBottomReachedListener(object :
//                                        OnBottomReachedListener() {
//                                        fun onBottomReached(position: Int) {
//                                            println("reachedbottom")
//                                            isFromBottom = true
//                                            scrollTo = "old"
//                                            val listSize = mPhotosModelArrayList!!.size
//                                            apiID =
//                                                mPhotosModelArrayList!![listSize - 1].getPhotoId()
//                                            println("Page From Value$apiID")
//                                            if (notificationSize == 15) {
//                                                if (AppUtils.isNetworkConnected(mContext)) {
//                                                    photosListApiCall(apiID, scrollTo)
//                                                } else {
//                                                    AppUtils.showDialogAlertDismiss(
//                                                        mContext as Activity,
//                                                        "Network Error",
//                                                        getString(R.string.no_internet),
//                                                        R.drawable.nonetworkicon,
//                                                        R.drawable.roundred
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    })
                                }else{
                                    Toast.makeText(mContext, "Gallery is empty", Toast.LENGTH_SHORT).show()
                                }
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

            override fun onFailure(call: Call<GetAlbumsResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE

                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })


    }

    private fun initilaiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        progressBar = findViewById(R.id.progress)
//        headermanager = HeaderManager(this@PhotosRecyclerViewActivity, "Photos")
//        headermanager.getHeader(relativeHeader, 1)
//        back = headermanager.getLeftButton()
//        headermanager.setButtonLeftSelector(
//            R.drawable.back,
//            R.drawable.back
//        )
//        back!!.setOnClickListener { finish() }
//        home = headermanager.getLogoButton()
//        home!!.setOnClickListener {
//            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }


        recycler_view_photos = findViewById<View>(R.id.recycler_view_photos) as RecyclerView
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        //        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_view_photos!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
//        int spacing = 10; // 50px
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
//        recycler_view_photos.addItemDecoration(itemDecoration);
//        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
        //        int spacing = 10; // 50px
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
//        recycler_view_photos.addItemDecoration(itemDecoration);
//        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider)!!)
        recycler_view_photos!!.addItemDecoration(divider)

//        recycler_view_photos.addItemDecoration(itemDecoration);
        //        recycler_view_photos.addItemDecoration(itemDecoration);
        recycler_view_photos!!.layoutManager = recyclerViewLayoutManager
        recycler_view_photos!!.addOnItemClickListener(object : OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(mContext, PhotosViewRecyclerViewActivity::class.java)
                //                            intent.putExtra("photo_array", mPhotosModelArrayList);
//                        AppController.mPhotosModelArrayListGallery=mPhotosModelArrayList;
                intent.putExtra("albumID", mPhotosModelArrayList!![position].id.toString())
                intent.putExtra("pos", position)
                startActivity(intent)
            }

        })
        mPhotosModelArrayList = java.util.ArrayList<GetAlbumsResponseModel.Data>()
    }
}