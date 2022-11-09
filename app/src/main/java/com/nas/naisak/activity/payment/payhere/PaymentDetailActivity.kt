package com.nas.naisak.activity.payment.payhere

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PrintJob
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.BuildConfig
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.payment.payhere.adapter.PaymentOptionAdapter
import com.nas.naisak.activity.payment.payhere.model.*
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentDetailActivity  : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var relativeHeader: RelativeLayout
    lateinit var backRelative: RelativeLayout
    lateinit var logoClickImgView: ImageView
    lateinit var btn_left: ImageView
    lateinit var heading: TextView
    lateinit var descriptionTitle: TextView

    lateinit var payTotalButton: Button
    lateinit var totalLinear: LinearLayout
    lateinit var mainLinear: LinearLayout
    lateinit var printLinear: LinearLayout
    lateinit var printLinearClick: LinearLayout
    lateinit var downloadLinear: LinearLayout
    lateinit var shareLinear: LinearLayout
    lateinit var paidImg: ImageView
    lateinit var paymentWeb: WebView
    lateinit var mProgressRelLayout: RelativeLayout
    lateinit var printJob: PrintJob
    lateinit var description: TextView
    lateinit var totalAmount: TextView

    var id: String = ""
    var title: String = ""
    var fullHtml: String = ""
    var payment_type_invoice: String = ""
    var current: String = ""
    var formated_amt: String = ""
    var descriptionTxt: String = ""
    var formated_amount: String = ""
    var student_name: String = ""
    var payment_date: String = ""
    var invoice_note: String = ""
    var isams_no: String = ""
    var trn_no: String = ""
    var payment_type_print: String = ""
    var invoice_description: String = ""
    var invoice_no: String = ""
    var payment_type:String=""
    var payment_id:String=""
    var is_paid:Int=0
    var PaymentID:Int=0
    lateinit var anim: RotateAnimation
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)
        mContext=this
        initUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            mProgressRelLayout.visibility= View.VISIBLE
            callPayementDetail()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }

    }
    fun initUI() {
        id = intent.getStringExtra("id").toString()
        title = intent.getStringExtra("title").toString()
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        descriptionTitle = findViewById(R.id.descriptionTitle)
        payTotalButton = findViewById(R.id.payTotalButton)
        totalLinear = findViewById(R.id.totalLinear)
        paidImg = findViewById(R.id.paidImg)
        mainLinear = findViewById(R.id.mainLinear)
        printLinear = findViewById(R.id.printLinear)
        downloadLinear = findViewById(R.id.downloadLinear)
        printLinearClick = findViewById(R.id.printLinearClick)
        paymentWeb = findViewById(R.id.paymentWeb)
        mProgressRelLayout = findViewById(R.id.progressDialog)
        shareLinear = findViewById(R.id.shareLinear)
        description = findViewById(R.id.description)
        totalAmount = findViewById(R.id.totalAmount)
        mProgressRelLayout.visibility= View.GONE
        heading.text = "Payment Details"
        descriptionTitle.text = title

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
        payTotalButton.setOnClickListener(View.OnClickListener {

            callOptionDialog(mContext)

        })

        printLinear.setOnClickListener(View.OnClickListener {
            mProgressRelLayout.visibility= View.VISIBLE
            callGenerateReceiptApi("print")
        })

      downloadLinear.setOnClickListener(View.OnClickListener {
          mProgressRelLayout.visibility= View.VISIBLE
          callGenerateReceiptApi("download")

        })

        shareLinear.setOnClickListener(View.OnClickListener {
            mProgressRelLayout.visibility= View.VISIBLE
          callGenerateReceiptApi("share")

        })



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
                if(url.contains("http://naisakweb.mobatia.in:5000/payment/Callback?success=True")){
                    Log.e("PAYMEBNT", "SUCCESS" + url)
                    var uri:Uri=Uri.parse(url)
                    var reference:String? =  uri.getQueryParameter("reference")
                    var PUN:String? =  uri.getQueryParameter("PUN")
                    mProgressRelLayout.visibility= View.VISIBLE
                    callPaymentSubmissionApi(reference, PUN)
                    return true
                }
                else{
                    return false
                }
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url)
                mProgressRelLayout.clearAnimation()
                mProgressRelLayout.visibility = View.GONE
            }

        }

    }

    fun callPayementDetail()
    {
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentDetailApiModel(id)
        val call: Call<PaymentDetailResponseModel> =
            ApiClient.getClient.paymentdetail(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<PaymentDetailResponseModel> {
            override fun onFailure(call: Call<PaymentDetailResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<PaymentDetailResponseModel>,
                response: Response<PaymentDetailResponseModel>
            )
            {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {
                    payment_type = response.body()!!.data.payment_type
                    if (payment_type.equals("1")) {
                        payment_type_invoice = "Online"
                    }
                    else if (payment_type.equals("2")) {
                        payment_type_invoice = "Cash"
                    }
                    else if (payment_type.equals("3")) {
                        payment_type_invoice = "Cheque"
                    }
                    else if (payment_type.equals("4")) {
                        payment_type_invoice = "Bank Transfer"
                    }
                    else if (payment_type.equals("5")) {
                        payment_type_invoice = "Online"
                    }
                    else {
                        payment_type_invoice = "Online"
                    }
                    PaymentID = response.body()!!.data.id
                    is_paid = response.body()!!.data.is_paid
                    formated_amount = response.body()!!.data.formated_amount
                    current = response.body()!!.data.amount
                    student_name = response.body()!!.data.student_name
                    payment_date = response.body()!!.data.payment_date
                    invoice_note = response.body()!!.data.invoice_note
                    isams_no = response.body()!!.data.isams_no
                    invoice_description = response.body()!!.data.invoice_description
                    payment_type_print = payment_type_invoice
                    trn_no = response.body()!!.data.trn_no
                    invoice_no = response.body()!!.data.invoice_no
                    payment_id = response.body()!!.data.payment_id
                    if (is_paid == 0) {
                        payTotalButton.visibility = View.VISIBLE
                        totalLinear.visibility = View.VISIBLE
                        paidImg.visibility = View.GONE
                        mainLinear.visibility = View.VISIBLE
                        printLinear.visibility = View.GONE
                    }
                    else {
                        if(payment_type.equals("1"))
                        {
                            payTotalButton.visibility = View.GONE
                            totalLinear.visibility = View.VISIBLE
                            paidImg.visibility = View.VISIBLE
                            paidImg.setImageResource(R.drawable.paid)
                            mainLinear.visibility = View.VISIBLE
                            printLinear.visibility = View.VISIBLE
                        }
                        else
                        {
                            payTotalButton.visibility = View.GONE
                            totalLinear.visibility = View.VISIBLE
                            paidImg.visibility = View.VISIBLE
                            paidImg.setImageResource(R.drawable.paid_at_school)
                            mainLinear.visibility = View.VISIBLE
                            printLinear.visibility = View.GONE
                        }

                    }
                    descriptionTxt = response.body()!!.data.invoice_description
                    description.text = descriptionTxt
                    formated_amt = response.body()!!.data.formated_amount
                    totalAmount.text = formated_amt

                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@PaymentDetailActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }

                else
                {

                }
            }

        })
    }
    fun callGatewayLink(paymentMethod:String)
    {
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentGatewayApiModel(
            PaymentID.toString(), current, invoice_no, PreferenceManager.getUserEmail(
                mContext
            )!!, paymentMethod, "2", "Android", "1.0"
        )
        val call: Call<PaymentGatewayResponseModel> =
            ApiClient.getClient.paymentGateway(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<PaymentGatewayResponseModel> {
            override fun onFailure(call: Call<PaymentGatewayResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<PaymentGatewayResponseModel>,
                response: Response<PaymentGatewayResponseModel>
            ) {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.lists.payment_url
                    var url = payment_url.replaceFirst(
                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
                        ""
                    )
                    mainLinear.visibility = View.GONE
                    paymentWeb.visibility = View.VISIBLE
                    setWebViewSettingsPrint()
                    Log.e("URL LOAD", url)
                    paymentWeb.loadUrl(url)

                }
                else
                {

                }
            }

        })
    }

    fun callPaymentSubmissionApi(merchantReference: String?, reference: String?)
    {
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentSubmissionApiModel(
            merchantReference.toString(),
            reference.toString()
        )
        val call: Call<PaymentSubmissionResponseModel> =
            ApiClient.getClient.paymentSubmission(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<PaymentSubmissionResponseModel> {
            override fun onFailure(call: Call<PaymentSubmissionResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<PaymentSubmissionResponseModel>,
                response: Response<PaymentSubmissionResponseModel>
            ) {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {

                    payment_type_print = "Online"
                    payTotalButton.visibility = View.GONE
                    totalLinear.visibility = View.VISIBLE
                    paidImg.visibility = View.VISIBLE
                    mainLinear.visibility = View.VISIBLE
                    printLinear.visibility = View.VISIBLE
                    paymentWeb.visibility=View.GONE
                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@PaymentDetailActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }
                else {


                }
            }

        })
    }

    fun callGenerateReceiptApi(click:String)
    {
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentReceiptApiModel(PaymentID.toString())
        val call: Call<GeneretReceiptResponseModel> =
            ApiClient.getClient.generateReceipt(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<GeneretReceiptResponseModel> {
            override fun onFailure(call: Call<GeneretReceiptResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<GeneretReceiptResponseModel>,
                response: Response<GeneretReceiptResponseModel>
            ) {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {

                    var receipt_url = response.body()!!.data.receipt
                    var url = receipt_url.replaceFirst(
                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
                        "")
                    Log.e("URL LOAD", url)

                    if(click.equals("print"))
                    {
                        val intent = Intent(mContext, PaymentPrintActivity::class.java)
                        intent.putExtra("webview_url", url)
                        intent.putExtra("title", invoice_no)
                        intent.putExtra("PaymentID", PaymentID)
                        startActivity(intent)
                    }
                    else if (click.equals("download"))
                    {
                        val intent = Intent(mContext, PaymentPdfReader::class.java)
                        intent.putExtra("pdf_url",url)
                        intent.putExtra("pdf_title", invoice_no)
                        startActivity(intent)
                    }
                    else if (click.equals("share"))
                    {
                        val intent = Intent(mContext, PaymentReceiptShare::class.java)
                        intent.putExtra("pdf_url",url)
                        intent.putExtra("pdf_title", invoice_no)
                        startActivity(intent)
                    }


                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@PaymentDetailActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }
                else {


                }
            }

        })
    }


    fun callOptionDialog(context: Context)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_credit_debit)
        var btn_Ok = dialog.findViewById(R.id.btn_dismiss) as Button
        lateinit var mPaymentOptionArrayList: ArrayList<String>
        var studentListRecycler = dialog.findViewById(R.id.recycler_view_social_media) as RecyclerView
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        mPaymentOptionArrayList=ArrayList()
        mPaymentOptionArrayList.add("Credit")
        mPaymentOptionArrayList.add("Debit")
        val settingsAdapter = PaymentOptionAdapter(mPaymentOptionArrayList)
        studentListRecycler.adapter = settingsAdapter

        btn_Ok.setOnClickListener()
        {

            dialog.dismiss()
        }

        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                if (CommonMethods.isInternetAvailable(mContext)) {
                    var method:String=""
                    if(position==0)
                    {
                        method="1"
                        mProgressRelLayout.visibility= View.VISIBLE
                        callCreditInitApi(method)
                    }
                    else if (position==1)
                    {
                        method="2"
                        callDebitInitApi(method)
                       // CommonMethods.showDialogueWithOkPay(context,"Currently not supported this type of payment","Alert")

                    }
                    else
                    {
                        method="3"
                        CommonMethods.showDialogueWithOkPay(context,"Currently not supported this type of payment","Alert")
                    }
//                    mProgressRelLayout.visibility= View.VISIBLE
//                    callGatewayLink(method)
                }
                else
                {
                    CommonMethods.showSuccessInternetAlert(mContext)
                }
                dialog.dismiss()
            }
        })

        dialog.show()
    }



    fun callCreditInitApi(paymentMethod:String)
    {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device=manufacturer+model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentGatewayApiModel(
            PaymentID.toString(), current, invoice_no, PreferenceManager.getUserEmail(
                mContext
            )!!, paymentMethod, "2", device, versionName
        )
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.paymentCreditInitiate(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(call: Call<PaymentGatewayCreditInitiateResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(mContext, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url",payment_url)
                    startActivity(intent)
//                    var url = payment_url.replaceFirst(
//                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
//                        ""
//                    )
//                    mainLinear.visibility = View.GONE
//                    paymentWeb.visibility = View.VISIBLE
//                    setWebViewSettingsPrint()
//                    Log.e("URL LOAD", url)
//                    paymentWeb.loadUrl(url)


                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@PaymentDetailActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }
                else
                {


                }
            }

        })
    }


    fun callDebitInitApi(paymentMethod:String)
    {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device=manufacturer+model
        val versionName: String = BuildConfig.VERSION_NAME
        val token = PreferenceManager.getUserCode(mContext)
        val paymentID = PaymentGatewayApiModel(
            PaymentID.toString(), current, invoice_no, PreferenceManager.getUserEmail(
                mContext
            )!!, paymentMethod, "2", device, versionName
        )
        val call: Call<PaymentGatewayCreditInitiateResponseModel> =
            ApiClient.getClient.paymentDebitInitiate(paymentID, "Bearer " + token)
        call.enqueue(object : Callback<PaymentGatewayCreditInitiateResponseModel> {
            override fun onFailure(call: Call<PaymentGatewayCreditInitiateResponseModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                mProgressRelLayout.visibility= View.GONE
            }

            override fun onResponse(
                call: Call<PaymentGatewayCreditInitiateResponseModel>,
                response: Response<PaymentGatewayCreditInitiateResponseModel>
            ) {
                mProgressRelLayout.visibility= View.GONE
                if (response.body()!!.status == 100) {

                    var payment_url = response.body()!!.data.redirect_url
                    val intent = Intent(mContext, PaymentPayActivity::class.java)
                    intent.putExtra("payment_url",payment_url)
                    startActivity(intent)
//                    var url = payment_url.replaceFirst(
//                        "^(http[s]?://www\\\\.|http[s]?://|www\\\\.)",
//                        ""
//                    )
//                    mainLinear.visibility = View.GONE
//                    paymentWeb.visibility = View.VISIBLE
//                    setWebViewSettingsPrint()
//                    Log.e("URL LOAD", url)
//                    paymentWeb.loadUrl(url)


                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@PaymentDetailActivity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }
                else
                {


                }
            }

        })
    }



    override fun onResume() {
        super.onResume()
        callPayementDetail()
    }
}