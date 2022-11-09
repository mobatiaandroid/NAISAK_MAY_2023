package com.nas.naisak.activity.payment.payhere

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class PaymentPrintActivity : AppCompatActivity() {
    lateinit var back: ImageView

    //lateinit var downloadpdf: ImageView
    lateinit var context: Context
    lateinit var webview: WebView
    lateinit var progressbar: ProgressBar
    var urltoshow: String = ""
    var titleToShow: String = ""
    var url: String = ""
    lateinit var logoclick: ImageView
    lateinit var printImg: ImageView
    lateinit var titleTextView: TextView
    lateinit var relativeHeader: RelativeLayout
    lateinit var backRelative: RelativeLayout
    lateinit var logoClickImgView: ImageView
    lateinit var btn_left: ImageView
    lateinit var heading: TextView
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_print)
        context = this
        printImg = findViewById(R.id.printImg)
        urltoshow = intent.getStringExtra("webview_url").toString()
        titleToShow = intent.getStringExtra("title").toString()
        webview = findViewById(R.id.webview)
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressbar = findViewById(R.id.progress)
        progressbar.visibility=View.GONE
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        webview.webViewClient = WebViewClient()
        webview.settings.setSupportZoom(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled=true

        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // TODO Auto-generated method stub
                view.loadUrl(url)
                return true
            }

        }
        try {
           url = URLEncoder.encode(urltoshow, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val path = "http://docs.google.com/gview?embedded=true&url=$url"
        webview.loadUrl(path)
      //  webview.loadUrl("https://docs.google.com/gview?embedded=true&url=$urltoshow")

        printImg.setOnClickListener(View.OnClickListener {
            createWebPrintJob(webview)
        })
    }


    fun createWebPrintJob(webView: WebView)
    {
        webView.visibility=View.VISIBLE
        val printManager = this.getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter()
        val jobName = titleToShow + " Print Test"
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }


}