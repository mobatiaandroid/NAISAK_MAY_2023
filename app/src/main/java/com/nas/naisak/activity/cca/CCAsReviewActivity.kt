package com.nas.naisak.activity.cca

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAfinalReviewAdapter
import com.nas.naisak.activity.cca.model.CCADetailModel
import com.nas.naisak.activity.cca.model.CCASubmitResponseModel
import com.nas.naisak.activity.cca.model.CCASumbitRequestModel
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.AppController
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CCAsReviewActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recycler_review: RecyclerView? = null
//    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var CCADetailModelArrayList: ArrayList<CCADetailModel>? = ArrayList()
//    var back: ImageView? = null
    var submitBtn: Button? = null
    var home: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null
    var mCCADetailModelArrayList: ArrayList<CCADetailModel>? = ArrayList()
    var mCCAItemIdArray: java.util.ArrayList<String>? = null
    var textViewCCAaItem: TextView? = null
    var cca_details = ""
    var cca_detailsId = "["
    var mCCADetailModels: CCADetailModel? = null
    var survey_satisfation_status = 0
    var currentPage = 0
    var currentPageSurvey = 0
    private val surveySize = 0
    var pos = -1
//    var surveyArrayList: java.util.ArrayList<SurveyModel>? = null
//    var surveyQuestionArrayList: java.util.ArrayList<SurveyQuestionsModel>? = null
//    var surveyAnswersArrayList: java.util.ArrayList<SurveyAnswersModel>? = null
//    var mAnswerList: java.util.ArrayList<AnswerSubmitModel>? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    private val surveyEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccas_review)
        mContext = this
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
        progressBar = findViewById(R.id.progress)
        extras = intent.extras
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        if (extras != null) {
//            tab_type = extras!!.getString("tab_type").toString()

            CCADetailModelArrayList=
                extras!!.getSerializable("detail_array") as ArrayList<CCADetailModel>?
            Log.e("size review", CCADetailModelArrayList!!.size.toString())
        }else{
           CCADetailModelArrayList = AppController.CCADetailModelArrayList
            Log.e("size review", CCADetailModelArrayList!!.size.toString())
        }

        for (j in 0 until CCADetailModelArrayList!!.size)
            {
                Log.e("ccadetail model size", CCADetailModelArrayList!!.size.toString())
        }

        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        textViewCCAaItem = findViewById<View>(R.id.textViewCCAaItem) as TextView
        submitBtn = findViewById<View>(R.id.submitBtn) as Button

        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
        mCCADetailModelArrayList = ArrayList<CCADetailModel>()
        mCCAItemIdArray = ArrayList<String>()
        if (PreferenceManager.getStudClassForCCA(mContext).equals("")) {
            textViewCCAaItem!!.text = Html.fromHtml(
                PreferenceManager.getCCATitle(mContext)
                    .toString() + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)
            )
        } else {
            textViewCCAaItem!!.text = Html.fromHtml(
                PreferenceManager.getCCATitle(mContext)
                    .toString() + "<br/>" + PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(
                    mContext
                )
            )
        }
        for (i in 0 until AppController.weekList.size) {
            Log.e("here","Here")
            for (j in 0 until CCADetailModelArrayList!!.size) {
                if (AppController.weekList[i].weekDay.equals(
                        CCADetailModelArrayList!![j].day,ignoreCase = true
                    )
                ) {
                    Log.e("ccadetail model size", CCADetailModelArrayList!!.size.toString())
                    val mCCADetailModel = CCADetailModel()
                    mCCADetailModel.day = CCADetailModelArrayList!![j].day
                    mCCADetailModel.choice1 = CCADetailModelArrayList!![j].choice1
                    mCCADetailModel.choice2 = CCADetailModelArrayList!![j].choice2
                    mCCADetailModel.choice1Id = CCADetailModelArrayList!![j].choice1Id
                    mCCADetailModel.choice2Id = CCADetailModelArrayList!![j].choice2Id
                    if(CCADetailModelArrayList!![j].location != null){
                        mCCADetailModel.location = CCADetailModelArrayList!![j].location
                    }else{
                        mCCADetailModel.location = ""
                    }
                    if(CCADetailModelArrayList!![j].location2 != null){
                        mCCADetailModel.location2 = CCADetailModelArrayList!![j].location2
                    }else{
                        mCCADetailModel.location2 = ""
                    }
                    if(CCADetailModelArrayList!![j].description != null){
                        mCCADetailModel.description = CCADetailModelArrayList!![j].description
                    }else{
                        mCCADetailModel.description = ""
                    }
                    if(CCADetailModelArrayList!![j].description2 != null){
                        mCCADetailModel.description2 = CCADetailModelArrayList!![j].description2
                    }else{
                        mCCADetailModel.description2 = ""
                    }

                    for (k in 0 until CCADetailModelArrayList!![j]
                        .ccaChoiceModel
                        .size) if (CCADetailModelArrayList!![j]
                            .choice1.equals(
                                CCADetailModelArrayList!![j].ccaChoiceModel[k].cca_item_name
                            )
                    ) {
                        if (CCADetailModelArrayList!![j].ccaChoiceModel[k]
                                .cca_item_start_time != null && CCADetailModelArrayList!![j].ccaChoiceModel[k].cca_item_end_time != null
                        ) {
                            mCCADetailModel.cca_item_start_timechoice1 = CCADetailModelArrayList!![j].ccaChoiceModel[k].cca_item_start_time
                            mCCADetailModel.cca_item_end_timechoice1 = CCADetailModelArrayList!![j].ccaChoiceModel[k].cca_item_end_time
                            break
                        }
                    }
                    for (k in 0 until CCADetailModelArrayList!![j]
                        .ccaChoiceModel2
                        .size) if (CCADetailModelArrayList!![j]
                            .choice2.equals(
                                CCADetailModelArrayList!![j].ccaChoiceModel2[k].cca_item_name,ignoreCase = true
                            )
                    ) {
                        if (CCADetailModelArrayList!![j].ccaChoiceModel2[k]
                                .cca_item_start_time != null && CCADetailModelArrayList!![j].ccaChoiceModel2[k].cca_item_end_time != null
                        ) {
                            mCCADetailModel.cca_item_start_timechoice2 = CCADetailModelArrayList!![j].ccaChoiceModel2[k].cca_item_start_time
                            mCCADetailModel.cca_item_end_timechoice2 = CCADetailModelArrayList!![j].ccaChoiceModel2[k].cca_item_end_time
                            break
                        }
                    }
                    mCCADetailModelArrayList!!.add(mCCADetailModel)
                    Log.e("detaiol",mCCADetailModel.toString())
                    break
                }
            }
        }

        val mCCAsActivityAdapter = CCAfinalReviewAdapter(mContext, mCCADetailModelArrayList)
        recycler_review!!.adapter = mCCAsActivityAdapter
        for (j in mCCADetailModelArrayList!!.indices) {
            Log.e("cca", mCCADetailModelArrayList!![j].choice1.toString())
            if (mCCADetailModelArrayList!!.get(j)
                    .choice1 != null && mCCADetailModelArrayList!![j].choice2 != null
            ) {
                if (!mCCADetailModelArrayList!![j].choice1Id.equals("-541") && !mCCADetailModelArrayList!![j].choice2Id.equals("-541")
                ) {
                    Log.e("1",
                        mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id).toString()
                    )

                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id)
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id)
                } else if (!mCCADetailModelArrayList!![j].choice1Id.equals("-541")
                ) {
                    Log.e("2",
                        mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id).toString()
                    )
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id)
                } else if (!mCCADetailModelArrayList!![j].choice2Id.equals("-541")
                ) {
                    Log.e("13",
                        mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id).toString()
                    )
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id)
                }
            } else if (mCCADetailModelArrayList!![j].choice1 != null) {
                if (!mCCADetailModelArrayList!![j].choice1Id.equals("-541")) {
                    Log.e("14",
                        mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id).toString()
                    )
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id)
                }
            } else if (mCCADetailModelArrayList!![j].choice2 != null) {
                if (!mCCADetailModelArrayList!![j].choice2Id.equals("-541")) {
                    Log.e("15",
                        mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id).toString()
                    )
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id)
                }
            }
        }

        if (mCCAItemIdArray!!.size == 0) {
            cca_detailsId += "]}"
        }
        for (i in mCCAItemIdArray!!.indices) {
            Log.e("items", mCCAItemIdArray!![i].toString())
            if (mCCAItemIdArray!!.size - 1 == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray!![i] + "\"]}"
            } else if (i == mCCAItemIdArray!!.size - 1) {
                cca_detailsId += mCCAItemIdArray!![i] + "\"]}"
            } else if (i == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray!![i] + "\",\""
            } else {
                cca_detailsId += mCCAItemIdArray!![i] + "\",\""
            }
        }
        cca_details = "{\"cca_days_id\":\"" + PreferenceManager.getCCAItemId(mContext)
            .toString() + "\",\"student_id\":\"" + PreferenceManager.getStudIdForCCA(mContext)
            .toString() + "\",\"users_id\":\"" + PreferenceManager.getUserCode(mContext)
            .toString() + "\",\"cca_days_details_id\":" + cca_detailsId

        submitBtn!!.setOnClickListener(View.OnClickListener {
            showDialogReviewSubmit(
                mContext as Activity,
                "Confirm",
                "Do you want to confirm this ECA?",
                R.drawable.exclamationicon,
                R.drawable.round
            )
        })
    }

    private fun showDialogReviewSubmit(activity: Activity, msgHead: String, msg: String, ico: Int, bgIcon: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead

        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            if (CommonMethods.isInternetAvailable(mContext)) {
                ccaSubmitAPI()
            } else {
                CommonMethods.showSuccessInternetAlert(mContext)

            }
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun ccaSubmitAPI() {
        Log.e("stud", PreferenceManager.getStudIdForCCA(mContext).toString())
        Log.e("day",PreferenceManager.getCCAItemId(mContext).toString())
        Log.e("details",cca_detailsId)
        val ccaDetail: ArrayList<String> = ArrayList()
        for (i in mCCAItemIdArray!!.indices){
//            if ( i != 0) {
            if(!mCCAItemIdArray!![i].equals("-541"))
                ccaDetail.add(mCCAItemIdArray!![i].toString())
//            }

        }
        Log.e("details1",ccaDetail.toString())

        var model= CCASumbitRequestModel(PreferenceManager.getStudIdForCCA(mContext).toString(),
            PreferenceManager.getCCAItemId(mContext).toString(),ccaDetail.toString()
        )
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<CCASubmitResponseModel> =
            ApiClient.getClient.ccaSubmit( model,"Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<CCASubmitResponseModel> {
            override fun onResponse(
                call: Call<CCASubmitResponseModel>,
                response: Response<CCASubmitResponseModel>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status!!.equals(100)){

//                            val survey: Int = secobj.optInt("survey")
                            showDialogAlert(
                                mContext as Activity,
                                "Success",
                                "You are able to make changes until the closing date. After the closing date selections are final",
                                R.drawable.tickicon,
                                R.drawable.round,
                            )

                        }
                        else if (response.body()!!.status!!.equals(109))
                        {


                        }
                        else{

                            Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
                        }

                    }else{

                        CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                    }
                }else{

                    CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<CCASubmitResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })
    }

    fun showDialogAlert(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int,
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
//            if (survey == 1) {
//                callSurveyApi()
//            } else {
                val intent = Intent(mContext, CCA_Activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
//            }
        }
        dialog.show()
    }
}