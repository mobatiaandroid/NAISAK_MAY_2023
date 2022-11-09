package com.nas.naisak.activity.gallery

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.nas.naisak.R
import com.nas.naisak.activity.gallery.adapter.ImagePagerAdapter
import com.nas.naisak.activity.gallery.adapter.PhotosRecyclerviewAdapter
import com.nas.naisak.activity.gallery.model.GetPhotoResponseModel

class PhotosViewPagerActivity : AppCompatActivity() {
    var mContext: Context? = null

    //    RelativeLayout relativeHeader;
    //    HeaderManager headermanager;
    var back: ImageView? = null

    //    ImageView home;
//    var intent: Intent? = null
    var mPhotosModelArrayList: ArrayList<GetPhotoResponseModel.Data?>? = null
    var mPhotosRecyclerviewAdapter: PhotosRecyclerviewAdapter? = null
    var extras: Bundle? = null
    var bannerImageViewPager: ViewPager? = null
    var pos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_view_pager)
        mContext = this
        initUI()
    }

    private fun initUI() {

//        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        back = findViewById<View>(R.id.back) as ImageView
        bannerImageViewPager = findViewById<View>(R.id.bannerImageViewPager) as ViewPager
        extras = intent.extras
        if (extras != null) {
            mPhotosModelArrayList =
                extras!!.getSerializable("photo_array") as ArrayList<GetPhotoResponseModel.Data?>?
            pos = extras!!.getInt("pos")
        }

        back!!.setOnClickListener { finish() }
       bannerImageViewPager!!.adapter =
            ImagePagerAdapter(mContext, mPhotosModelArrayList, "portrait")
        bannerImageViewPager!!.currentItem = pos
        bannerImageViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                headermanager.setTitle((position + 1) + " Of " + mPhotosModelArrayList.size());
                pos = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }
}