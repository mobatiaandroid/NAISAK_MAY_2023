package com.nas.naisak.activity.notification

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.notification.model.MessageDetailResponse
import com.nas.naisak.activity.notification.model.NotificationDetailApiModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

lateinit var proWebView: ProgressBar
lateinit var textcontent: WebView
class VideoNotificationActivity : AppCompatActivity(){
    lateinit var mContext: Context
    var id:String=""
    var title:String=""
    var idApi:String=""
    var titleApi:String=""
    var message:String=""
    var url:String=""
    var date:String=""
     lateinit var relativeHeader: RelativeLayout
     lateinit var backRelative: RelativeLayout
     lateinit var logoClickImgView: ImageView
     lateinit var btn_left: ImageView
     lateinit var heading: TextView

     lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_notification)
        mContext=this
        id=intent.getStringExtra("id").toString()
        title=intent.getStringExtra("title").toString()
        initUI()
        getSettings()
        if (CommonMethods.isInternetAvailable(mContext)) {
            callMessageDetailAPI()

        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }


    }
    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        textcontent = findViewById(R.id.txtContent)
        webView = findViewById(R.id.webView)
        proWebView = findViewById(R.id.proWebView)
        backRelative = findViewById(R.id.backRelative)
        textcontent.visibility= View.INVISIBLE
        heading.text = "Notification"
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

    }
    fun callMessageDetailAPI()
    {
        val token = PreferenceManager.getUserCode(mContext)
        val studentbody= NotificationDetailApiModel(id)
        val call: Call<MessageDetailResponse> = ApiClient.getClient.notificationdetail(
            studentbody,
            "Bearer " + token
        )
        call.enqueue(object : Callback<MessageDetailResponse> {
            override fun onFailure(call: Call<MessageDetailResponse>, t: Throwable) {
                proWebView.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<MessageDetailResponse>,
                response: Response<MessageDetailResponse>
            ) {
                proWebView.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    idApi = id
                    titleApi = title
                    message = response.body()!!.data.message
                    url = response.body()!!.data.url
                    date = response.body()!!.data.time_stamp
                    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                    val outputFormatdate: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
                    val inputDateStr = date
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    val outputDateStr1: String = outputFormatdate.format(date)
                    var pushNotificationDetail = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<style>\n" +
                            "\n" +
                            "@font-face {\n" +
                            "font-family: SourceSansPro-Semibold;" +
                            "src: url(SourceSansPro-Semibold.ttf);" +

                            "font-family: SourceSansPro-Regular;" +
                            "src: url(SourceSansPro-Regular.ttf);" +
                            "}" +
                            ".title {" +
                            "font-family: SourceSansPro-Regular;" +
                            "font-size:16px;" +
                            "text-align:left;" +
                            "color:	#46C1D0;" +
                            "text-align: ####TEXT_ALIGN####;" +
                            "}" +
                            ".description {" +
                            "font-family: SourceSansPro-Semibold;" +
                            "text-align:justify;" +
                            "font-size:14px;" +
                            "color: #000000;" +
                            "text-align: ####TEXT_ALIGN####;" +
                            "}" +
                            "</style>\n" + "</head>" +
                            "<body>" +
                            "<p class='title'>" + title

                    pushNotificationDetail =
                        pushNotificationDetail + "<p class='description'>" + outputDateStr1 + " " + outputDateStr + "</p>"
                    if (!url.equals("")) {
                        pushNotificationDetail =
                            pushNotificationDetail + "<center><img src='" + url + "'width='100%', height='auto'>"
                    }
                    pushNotificationDetail = pushNotificationDetail + "</body>\n</html>"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }

//                    webView.webViewClient = HelloWebViewClient()
//                    webView.webChromeClient = WebChromeClient()
//                    webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
//                    webView.settings.javaScriptEnabled = true
//                    webView.settings.builtInZoomControls = false
//                    webView.settings.displayZoomControls = true
//                    webView.webViewClient = HelloWebViewClient()
//                    textcontent.settings.javaScriptEnabled = true
//                    textcontent.settings.pluginState = WebSettings.PluginState.ON
//                    textcontent.settings.builtInZoomControls = false
//                    textcontent.settings.displayZoomControls = true
                    textcontent.loadData(
                        pushNotificationDetail,
                        "text/html; charset=utf-8",
                        "utf-8"
                    )
                    var frameVideo = "<html>" + "<br><iframe width=\"360\" height=\"400\" src=\""
                    var url_Video =
                        frameVideo + url + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
                    var urlYoutube = url_Video.replace("watch?v=", "embed/")
                    webView.loadData(urlYoutube, "text/html", "utf-8")
                    proWebView.visibility = View.GONE

                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@VideoNotificationActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }

            }

        })
    }

    fun getSettings()
    {
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()

//        webView.settings.javaScriptEnabled = true
//        webView.settings.setSupportZoom(false)
//        webView.settings.setAppCacheEnabled(false)
//        webView.settings.javaScriptCanOpenWindowsAutomatically = true
//        webView.settings.domStorageEnabled = true
//        webView.settings.databaseEnabled = true
//        webView.settings.defaultTextEncodingName = "utf-8"
//        webView.settings.loadsImagesAutomatically = true
//        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webView.settings.allowFileAccess = true
//        webView.setBackgroundColor(Color.TRANSPARENT)
//        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
//
//
//        webView.webChromeClient = object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                proWebView.visibility = View.VISIBLE
//                println("testing2")
//                if (newProgress == 100)
//                {
//                    println("testing1")
//                    proWebView.visibility = View.GONE
//
//                }
//            }
        }
    }
//    private class HelloWebViewClient : WebViewClient() {
//        override fun shouldOverrideUrlLoading(
//            view: WebView,
//            url: String
//        ): Boolean {
//            return true
//        }
//
//        override fun onPageFinished(view: WebView, url: String) {
//            // TODO Auto-generated method stub
//            super.onPageFinished(view, url)
//            proWebView.setVisibility(View.GONE)
//            textcontent.visibility = View.VISIBLE
//        }
//    }
