package com.nas.naisak.activity.cca

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAfinalReviewEditAfterSubmissionAdapter
import com.nas.naisak.activity.cca.model.*
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CCAsReviewEditAfterSubmissionActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recycler_review: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    var home: ImageView? = null
    var editCcca: RelativeLayout? = null
    var messageTxt: RelativeLayout? = null
    var msgTxt: TextView? = null
    var tab_type = "ECAs"
    var extras: Bundle? = null
    var mCCADetailModelArrayList: ArrayList<CCAReviewAfterSubmissionModel>? = null
    var textViewCCAaItem: TextView? = null
    var weekList: ArrayList<String>? = null
    var absentDaysChoice2Array: ArrayList<String>? = null
    var presentDaysChoice2Array: ArrayList<String>? = null
    var upcomingDaysChoice2Array: ArrayList<String>? = null
    var absentDaysChoice1Array: ArrayList<String>? = null
    var presentDaysChoice1Array: ArrayList<String>? = null
    var upcomingDaysChoice1Array: ArrayList<String>? = null
    var datestringChoice1: ArrayList<CCAAttendanceModel>? = null
    var datestringChoice2: ArrayList<CCAAttendanceModel>? = null
    var submissiondateover = "-1"
    var CCADetailModelArrayList: ArrayList<CCADetailModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccas_review_edit_after_submission)
        mContext = this
        progressBar = findViewById(R.id.progress)
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
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
            tab_type = extras!!.getString("tab_type")!!
            submissiondateover = extras!!.getString("submissiondateover", "-1")
            CCADetailModelArrayList =
                extras!!.getSerializable("CCA_Detail") as ArrayList<CCADetailModel>?
        }
        weekList = java.util.ArrayList()
        weekList!!.add("Sunday")
        weekList!!.add("Monday")
        weekList!!.add("Tuesday")
        weekList!!.add("Wednesday")
        weekList!!.add("Thursday")
        weekList!!.add("Friday")
        weekList!!.add("Saturday")
        absentDaysChoice2Array = java.util.ArrayList()
        presentDaysChoice2Array = java.util.ArrayList()
        upcomingDaysChoice2Array = java.util.ArrayList()
        absentDaysChoice1Array = java.util.ArrayList()
        presentDaysChoice1Array = java.util.ArrayList()
        upcomingDaysChoice1Array = java.util.ArrayList()
        datestringChoice1 = java.util.ArrayList()
        datestringChoice2 = java.util.ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        textViewCCAaItem = findViewById<View>(R.id.textViewCCAaItem) as TextView
        messageTxt = findViewById<View>(R.id.messageTxt) as RelativeLayout
        msgTxt = findViewById<View>(R.id.msgTxt) as TextView

        messageTxt!!.visibility = View.VISIBLE

        editCcca = findViewById<View>(R.id.editCcca) as RelativeLayout

        editCcca!!.setOnClickListener {
            val intent = Intent(mContext, CCASelectionActivity::class.java)
            intent.putExtra(
                "CCA_Detail",
                CCADetailModelArrayList
            )
            intent.putExtra("tab_type", tab_type)
            intent.putExtra("ccaedit", 1)
            startActivity(intent)
        }
//        home = headermanager.getLogoButton()
//        home!!.setOnClickListener {
//            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }
        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
        mCCADetailModelArrayList = java.util.ArrayList()
//        textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        //        textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
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
        if (CommonMethods.isInternetAvailable(mContext)) {
            ccaReviewListAPI()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun ccaReviewListAPI() {
        val body = CCAReviewRequestModel(
            PreferenceManager.getStudIdForCCA(mContext)!!,
            PreferenceManager.getCCAItemId(mContext)!!
        )
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<CCAReviewResponseModel> =
            ApiClient.getClient.ccaReview(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<CCAReviewResponseModel> {
            override fun onResponse(
                call: Call<CCAReviewResponseModel>,
                response: Response<CCAReviewResponseModel>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()!!.status.toString() == "100") {

                            if (response.body()!!.data!!.cca_reviews!!.size > 0) {
                                for (j in weekList!!.indices) {
                                    for (i in 0 until response.body()!!.data!!.cca_reviews!!.size) {
                                        if (response.body()!!.data!!.cca_reviews!![i]!!.day.equals(
                                                weekList!!.get(j)
                                                    .toString(), ignoreCase = true
                                            )
                                        ) {
                                            addCCAReviewlist(
                                                response.body()!!.data!!.cca_reviews!![i]
                                            )
                                        }
                                    }
                                }
                                val mCCAsActivityAdapter = CCAfinalReviewEditAfterSubmissionAdapter(
                                    mContext,
                                    mCCADetailModelArrayList
                                )
                                recycler_review!!.setAdapter(
                                    mCCAsActivityAdapter
                                )
                            } else {
                                val mCCAsActivityAdapter = CCAfinalReviewEditAfterSubmissionAdapter(
                                    mContext,
                                    mCCADetailModelArrayList
                                )
                                recycler_review!!.setAdapter(
                                    mCCAsActivityAdapter
                                )
                                Toast.makeText(
                                    mContext,
                                    "No ECA available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                getString(R.string.common_error),
                                "Alert"
                            )
                        }
                    } else {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            getString(R.string.common_error),
                            "Alert"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CCAReviewResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                CommonMethods.showDialogueWithOk(
                    mContext,
                    getString(R.string.common_error),
                    "Alert"
                )
            }


        })
    }

    private fun addCCAReviewlist(dataObject: CCAReviewResponseModel.Data.CcaReview?) {
        val mCCAModel = CCAReviewAfterSubmissionModel()
        mCCAModel.day = dataObject!!.day
        datestringChoice1 = java.util.ArrayList()
        datestringChoice2 = java.util.ArrayList()
        if (dataObject.choice1 != null) {
            val choice1  = dataObject.choice1
            if (choice1 != null) {
                if (choice1.cca_item_name != null) {
                    mCCAModel.choice1 = choice1.cca_item_name
                    mCCAModel.cca_item_start_time = choice1.cca_item_start_time
                    mCCAModel.cca_item_end_time = choice1.cca_item_end_time
                    if (choice1.cca_item_description != null){
                        mCCAModel.cca_item_description = choice1.cca_item_description
//                        mCCAModel.cca_item_description_2 = ""
                    }else{
                        mCCAModel.cca_item_description = ""
//                        mCCAModel.cca_item_description_2 = ""
                    }
                    if (choice1.cca_venue != null){
                        mCCAModel.venue = choice1.cca_venue
//                        mCCAModel.venue2 = ""
                    }else{
                        mCCAModel.venue = ""
//                        mCCAModel.venue2 = ""
                    }

                    Log.e("des1",choice1.cca_item_description.toString())
                    mCCAModel.venue2 = ""
                    mCCAModel.cca_item_description_2 = ""
                    val absentDaysChoice1 = choice1.absentDays
                    absentDaysChoice1Array = java.util.ArrayList()
                    if (choice1.absentDays !=null) {
                        for (i in 0 until absentDaysChoice1!!.size) {
                            absentDaysChoice1Array!!.add(absentDaysChoice1[i]!!)
                        }
                    }
                    presentDaysChoice1Array = java.util.ArrayList()
                    if (choice1.presentDays != null) {
                        val presentDaysChoice1 = choice1.presentDays
                        for (i in 0 until presentDaysChoice1.size) {
                            presentDaysChoice1Array!!.add(presentDaysChoice1[i]!!)
                        }
                    }
                    upcomingDaysChoice1Array = java.util.ArrayList()
                    if (choice1.upcomingDays !=null) {
                        val upcomingDaysChoice1 = choice1.upcomingDays
                        for (i in 0 until upcomingDaysChoice1.size) {
                            upcomingDaysChoice1Array!!.add(upcomingDaysChoice1[i]!!)
                        }
                    }
                } else {
                    mCCAModel.choice1 = "0"
                }
            } else {
                mCCAModel.choice1 = "0"
            }
        } else {
            mCCAModel.choice1 = "-1"
        }
        if (dataObject.choice2 != null) {
            val choice2 = dataObject.choice2
            if (choice2 != null) {
                if (choice2.cca_item_name != null) {
                    mCCAModel.choice2 = choice2.cca_item_name
                    mCCAModel.cca_item_start_time = choice2.cca_item_start_time
                    mCCAModel.cca_item_end_time = choice2.cca_item_end_time
                    val absentDaysChoice2 = choice2.absentDays
                    Log.e("des",choice2.cca_item_description.toString())
                    if (choice2.cca_item_description != null){
                        mCCAModel.cca_item_description_2 = choice2.cca_item_description
//                        mCCAModel.cca_item_description = ""
                    }else{
                        mCCAModel.cca_item_description_2 = ""
//                        mCCAModel.cca_item_description = ""
                    }
                    if (choice2.cca_venue != null){
                        mCCAModel.venue2 = choice2.cca_venue
//                        mCCAModel.venue = ""
                    }else{
                        mCCAModel.venue2 = ""
//                        mCCAModel.venue = ""
                    }

                    if (choice2.absentDays != null) {
                        absentDaysChoice2Array = java.util.ArrayList()
                        for (i in 0 until absentDaysChoice2!!.size) {
                            absentDaysChoice2Array!!.add(absentDaysChoice2[i]!!)
                        }
                    }
                    presentDaysChoice2Array = java.util.ArrayList()
                    val presentDaysChoice2 = choice2.presentDays
                    if (choice2.presentDays != null) {
                        for (i in 0 until presentDaysChoice2!!.size) {
                            presentDaysChoice2Array!!.add(presentDaysChoice2[i]!!)
                        }
                    }
                    upcomingDaysChoice2Array = java.util.ArrayList()
                    val upcomingDaysChoice2 = choice2.upcomingDays
                    if (choice2.upcomingDays != null) {
                        for (i in 0 until upcomingDaysChoice2!!.size) {
                            upcomingDaysChoice2Array!!.add(upcomingDaysChoice2[i]!!)
                        }
                    }
                } else {
                    mCCAModel.choice2 = "0"
                }
            } else {
                mCCAModel.choice2 = "0"
            }
        } else {
            mCCAModel.choice2 = "-1"
        }

        if (absentDaysChoice1Array!!.size > 0) {
            for (i in absentDaysChoice1Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = absentDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA = "a"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (upcomingDaysChoice1Array!!.size > 0) {
            for (i in upcomingDaysChoice1Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = upcomingDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA = "u"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (presentDaysChoice1Array!!.size > 0) {
            for (i in presentDaysChoice1Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = presentDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA = "p"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }
        if (absentDaysChoice2Array!!.size > 0) {
            for (i in absentDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = absentDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA = "a"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (upcomingDaysChoice2Array!!.size > 0) {
            for (i in upcomingDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = upcomingDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA = "u"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (presentDaysChoice2Array!!.size > 0) {
            for (i in presentDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend = presentDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA = "p"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (datestringChoice1!!.size > 0) {
            Collections.sort(datestringChoice1, object : Comparator<CCAAttendanceModel?> {

                override fun compare(s1: CCAAttendanceModel?, s2: CCAAttendanceModel?): Int {
                    return s1!!.dateAttend.compareTo(s2!!.dateAttend)
                }
            })
        }
        if (datestringChoice2!!.size > 0) {
            Collections.sort(datestringChoice1, object : Comparator<CCAAttendanceModel?> {

                override fun compare(s1: CCAAttendanceModel?, s2: CCAAttendanceModel?): Int {
                    return s1!!.dateAttend.compareTo(s2!!.dateAttend)
                }
            })
        }
        mCCAModel.calendarDaysChoice1 = datestringChoice1
        mCCAModel.calendarDaysChoice2 = datestringChoice2

            mCCADetailModelArrayList!!.add(mCCAModel)

    }

}