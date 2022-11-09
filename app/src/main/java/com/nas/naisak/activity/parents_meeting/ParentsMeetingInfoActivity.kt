package com.nas.naisak.activity.parents_meeting

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.nas.naisak.R
import com.nas.naisak.activity.tutorial.TutorialViewPagerAdapter

class ParentsMeetingInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_meeting_info)
        mContext = this

        bannerarray.add(R.drawable.tut_pe_1)
        bannerarray.add(R.drawable.tut_pe_2)
        bannerarray.add(R.drawable.tut_pe_3)
        bannerarray.add(R.drawable.tut_pe_4)
        bannerarray.add(R.drawable.tut_pe_5)
        bannerarray.add(R.drawable.tut_pe_6)
        bannerarray.add(R.drawable.tut_pe_7)
        bannerarray.add(R.drawable.tut_pe_8)
        bannerarray.add(R.drawable.tut_pe_9)
        initializeUI()
    }
    lateinit var mContext: Context
    lateinit var tutorialViewPager: ViewPager
    lateinit var imageSkip: ImageView
    lateinit var mLinearLayout: LinearLayout
    lateinit var mImgCircle: Array<ImageView?>
    lateinit var progressBar: ProgressBar
    //   lateinit var  mImgCircle[]: ImageView
    var bannerarray = ArrayList<Int>()
    var bannerarrayCal = ArrayList<Int>()
    lateinit var isFrom:String
    var dataType: Int = 0


    private fun initializeUI() {
        isFrom = intent.getStringExtra("isFrom").toString()
        // headermanager=HeaderManagerNoColorSpace(SocialMediaDetailActivity.this, "FACEBOOK");
        tutorialViewPager = findViewById(R.id.tutorialViewPager)
        imageSkip = findViewById(R.id.imageSkip)
        mLinearLayout = findViewById(R.id.linear)
        progressBar = findViewById(R.id.progress)
        mImgCircle = arrayOfNulls(bannerarray.size)
        val mTutorialViewPagerAdapter = TutorialViewPagerAdapter(mContext, bannerarray)
        tutorialViewPager.currentItem = 0
        tutorialViewPager.adapter = mTutorialViewPagerAdapter
        addShowCountView(0)
        imageSkip.setOnClickListener {
            if (isFrom.equals("login"))
            {
                finish()
            }
            else{
                finish()
            }

        }
        tutorialViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                for (i in bannerarray.indices) {
                    mImgCircle[i]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.blackround)
                    )
                }
                if (position < bannerarray.size) {
                    mImgCircle[position]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.redround)
                    )
                    mLinearLayout.removeAllViews()
                    addShowCountView(position)
                } else {
                    mLinearLayout.removeAllViews()
                    finish()
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        tutorialViewPager.adapter!!.notifyDataSetChanged()
    }


    private fun addShowCountView(count: Int) {
        for (i in bannerarray.indices) {
            mImgCircle[i] = ImageView(mContext)
            val layoutParams = LinearLayout.LayoutParams(
                resources
                    .getDimension(R.dimen.home_circle_width).toInt(),
                resources.getDimension(
                    R.dimen.home_circle_height
                ).toInt()
            )
            mImgCircle.get(i)!!.layoutParams = layoutParams
            if (i == count) {
                mImgCircle.get(i)!!.setBackgroundResource(R.drawable.redround)
            } else {
                mImgCircle.get(i)!!.setBackgroundResource(R.drawable.blackround)
            }
            mLinearLayout.addView(mImgCircle.get(i))
        }
    }
}