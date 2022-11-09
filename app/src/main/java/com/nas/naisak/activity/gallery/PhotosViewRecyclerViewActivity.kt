package com.nas.naisak.activity.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.adapter.PhotosViewRecyclerviewAdapter
import com.nas.naisak.activity.gallery.model.GetPhotoRequestModel
import com.nas.naisak.activity.gallery.model.GetPhotoResponseModel
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotosViewRecyclerViewActivity : AppCompatActivity() {
    var mContext: Context = this
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar
    private var recycler_view_photos: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
//    var headermanager: HeaderManager? = null
//    var intent: Intent? = null
    var albumID = ""
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var mPhotosModelArrayList: ArrayList<GetPhotoResponseModel.Data>? = null
//    var mPhotosRecyclerviewAdapter: PhotosViewRecyclerviewAdapter? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    var extras: Bundle? = null
    var pos = 0
    var scrollTo = ""
    var apiID = ""
    var isFromBottom = false
    var notificationSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_view_recycler_view)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            mPhotosModelArrayList = java.util.ArrayList<GetPhotoResponseModel.Data>()
            albumID = extras!!.getString("albumID")!!
            pos = extras!!.getInt("pos")
        }
        initUI()
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        if (CommonMethods.isInternetAvailable(mContext)) {
            scrollTo = ""
            apiID = ""
            isFromBottom = false
            photosListApiCall(apiID, scrollTo)
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun photosListApiCall(apiID: String, scrollTo: String) {
        val body = GetPhotoRequestModel(albumID.toString())
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<GetPhotoResponseModel> =
            ApiClient.getClient.getPhotos(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<GetPhotoResponseModel> {
            override fun onResponse(
                call: Call<GetPhotoResponseModel>,
                response: Response<GetPhotoResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.isNotEmpty()){
                                for (i in response.body()!!.data!!.indices){
                                    mPhotosModelArrayList!!.add(response.body()!!.data!![i]!!)
                                }
                                val mPhotosRecyclerviewAdapter =
                                    PhotosViewRecyclerviewAdapter(mContext, mPhotosModelArrayList)
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

            override fun onFailure(call: Call<GetPhotoResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE

                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })


    }

    private fun initUI() {
        logoclick = findViewById(R.id.logoClickImgView)
        backRelative = findViewById(R.id.backRelative)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        progressBar = findViewById(R.id.progress)
//        headermanager = HeaderManager(this@PhotosViewRecyclerViewActivity, "Photos")
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
        /*extras=getIntent().getExtras();
        if(extras!=null){
            photo_id=extras.getString("photo_id");
        }*/
        /*extras=getIntent().getExtras();
        if(extras!=null){
            photo_id=extras.getString("photo_id");
        }*/recycler_view_photos = findViewById<View>(R.id.recycler_view_photos) as RecyclerView
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        //        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_view_photos!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 3)
        val spacing = 10 // 50px
        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recycler_view_photos!!.addItemDecoration(itemDecoration)
//        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recycler_view_photos!!.addItemDecoration(itemDecoration)
//        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
//        recycler_view_photos.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        //        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
//        recycler_view_photos.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_view_photos!!.layoutManager = recyclerViewLayoutManager
        recycler_view_photos!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {

                val intent = Intent(mContext, PhotosViewPagerActivity::class.java)
                intent.putExtra("photo_array", mPhotosModelArrayList)
                intent.putExtra("pos", position)
                startActivity(intent)
            }

        })


    }
}