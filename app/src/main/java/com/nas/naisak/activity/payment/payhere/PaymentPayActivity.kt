package com.nas.naisak.activity.payment.payhere

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity

class PaymentPayActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var relativeHeader: RelativeLayout
    lateinit var backRelative: RelativeLayout
    lateinit var logoClickImgView: ImageView
    lateinit var btn_left: ImageView
    lateinit var heading: TextView
    lateinit var paymentWeb: WebView
    lateinit var mProgressRelLayout: RelativeLayout
    lateinit var description: TextView
    var payment_url: String = ""
    var title: String = ""
    var current: String = ""
    lateinit var anim: RotateAnimation
    val PAYMENT_TIME_OUT:Long = 13000
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_pay)
        mContext=this
        initUI()
    }
    fun initUI() {
        payment_url = intent.getStringExtra("payment_url").toString()
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        paymentWeb = findViewById(R.id.paymentWeb)
        paymentWeb.visibility=View.VISIBLE
        mProgressRelLayout = findViewById(R.id.progressDialog)
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        var url = payment_url.replaceFirst(
            "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
            ""
        )
        setWebViewSettingsPrint()
        Log.e("URL LOAD", url)
        paymentWeb.loadUrl(url)

    }
    private fun setWebViewSettingsPrint() {
        mProgressRelLayout.visibility = View.VISIBLE
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.setInterpolator(mContext, android.R.interpolator.linear)
        anim.repeatCount = Animation.INFINITE
        anim.duration = 1000
        mProgressRelLayout.animation = anim
        mProgressRelLayout.startAnimation(anim)
        paymentWeb.settings.javaScriptEnabled = true
        paymentWeb.clearCache(true)
        paymentWeb.settings.domStorageEnabled = true
        paymentWeb.settings.javaScriptCanOpenWindowsAutomatically = true
        paymentWeb.settings.setSupportMultipleWindows(true)
        paymentWeb.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // TODO Auto-generated method stub
                view.loadUrl(url)
                Log.e("IT WORKING", "PAYMENT")
                if(url.contains("http://naisakweb.mobatia.in:5000/payment/credit/callback"))
                {
                    Handler(Looper.myLooper()!!).postDelayed({
                           finish()

                    }, PAYMENT_TIME_OUT)
                }
                if(url.contains("http://naisakweb.mobatia.in:5000/payment/Callback?success=True")){
                    Log.e("PAYMEBNT", "SUCCESS" + url)
                    var uri:Uri=Uri.parse(url)
                    var reference:String? =  uri.getQueryParameter("reference")
                    var PUN:String? =  uri.getQueryParameter("PUN")
                    return true
                }
                else{
                    return false
                }
                return false
            }

            override fun onPageFinished(view: WebView, url: String)
            {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url)
                mProgressRelLayout.clearAnimation()
                mProgressRelLayout.visibility = View.GONE
            }

        }
    }



}