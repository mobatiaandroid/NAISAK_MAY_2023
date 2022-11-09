package com.nas.naisak.activity.cca

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAfinalReviewAfterSubmissionAdapter
import com.nas.naisak.activity.cca.model.CCAAttendanceModel
import com.nas.naisak.activity.cca.model.CCAReviewAfterSubmissionModel
import com.nas.naisak.activity.cca.model.CCAReviewRequestModel
import com.nas.naisak.activity.cca.model.CCAReviewResponseModel
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CCAsReviewAfterSubmissionActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recycler_review: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    var attendanceListIcon: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null
    var mCCADetailModelArrayList: ArrayList<CCAReviewAfterSubmissionModel>? = null
    var textViewCCAaItem: TextView? = null
    var messageTxt: RelativeLayout? = null
    var editCcca: RelativeLayout? = null
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccas_review_after_submission)
        mContext = this
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
        progressBar = findViewById(R.id.progress)
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
            submissiondateover = extras!!.getString("submissiondateover", "-1")
        }
        weekList = java.util.ArrayList<String>()
        weekList!!.add("Sunday")
        weekList!!.add("Monday")
        weekList!!.add("Tuesday")
        weekList!!.add("Wednesday")
        weekList!!.add("Thursday")
        weekList!!.add("Friday")
        weekList!!.add("Saturday")
        absentDaysChoice2Array = java.util.ArrayList<String>()
        presentDaysChoice2Array = java.util.ArrayList<String>()
        upcomingDaysChoice2Array = java.util.ArrayList<String>()
        absentDaysChoice1Array = java.util.ArrayList<String>()
        presentDaysChoice1Array = java.util.ArrayList<String>()
        upcomingDaysChoice1Array = java.util.ArrayList<String>()
        datestringChoice1 =
            java.util.ArrayList<CCAAttendanceModel>()
        datestringChoice2 =
            java.util.ArrayList<CCAAttendanceModel>()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review =
            findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        textViewCCAaItem = findViewById<View>(R.id.textViewCCAaItem) as TextView
        messageTxt = findViewById<View>(R.id.messageTxt) as RelativeLayout
        editCcca = findViewById<View>(R.id.editCcca) as RelativeLayout
        editCcca!!.visibility = View.GONE
        messageTxt!!.visibility = View.GONE

        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        recycler_review!!.setLayoutManager(recyclerViewLayoutManager)
        mCCADetailModelArrayList =
            java.util.ArrayList<CCAReviewAfterSubmissionModel>()
        if (PreferenceManager.getStudClassForCCA(mContext)
                .equals("")
        ) {
            textViewCCAaItem!!.text = Html.fromHtml(
                PreferenceManager.getCCATitle(mContext)
                    .toString() + "<br/>" + PreferenceManager.getStudNameForCCA(
                    mContext
                )
            )
        } else {
            textViewCCAaItem!!.text = Html.fromHtml(
                PreferenceManager.getCCATitle(mContext)
                    .toString() + "<br/>" + PreferenceManager.getStudNameForCCA(
                    mContext
                ) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(
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
                                val mCCAsActivityAdapter = CCAfinalReviewAfterSubmissionAdapter(
                                    mContext,
                                    mCCADetailModelArrayList
                                )
                                recycler_review!!.setAdapter(
                                    mCCAsActivityAdapter
                                )
                            } else {
                                val mCCAsActivityAdapter = CCAfinalReviewAfterSubmissionAdapter(
                                    mContext,
                                    mCCADetailModelArrayList
                                )
                                recycler_review!!.setAdapter(
                                    mCCAsActivityAdapter
                                )
                                Toast.makeText(
                                    mContext,
                                    "No CCA available",
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
        datestringChoice1 =
            java.util.ArrayList<CCAAttendanceModel>()
        datestringChoice2 =
            java.util.ArrayList<CCAAttendanceModel>()
        var ch = 0
        if (dataObject!!.choice1 != null) {
            val choice1 = dataObject.choice1
            if (choice1 != null) {
                if (choice1.cca_item_name != null) {
                    mCCAModel.choice1 = choice1.cca_item_name
                    mCCAModel.cca_details_id = choice1.cca_details_id.toString()
                    mCCAModel.cca_item_start_time = choice1.cca_item_start_time
                    mCCAModel.cca_item_end_time = choice1.cca_item_end_time
                    mCCAModel.attending_status = choice1.attending_status
                    val absentDaysChoice1 = choice1.absentDays
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
                    absentDaysChoice1Array =
                        java.util.ArrayList<String>()
                    presentDaysChoice1Array =
                        java.util.ArrayList<String>()
                    upcomingDaysChoice1Array =
                        java.util.ArrayList<String>()
                    if (choice1.attending_status.equals("1", ignoreCase = true)) {
                        if (choice1.absentDays !=null) {
                            ch = 1
                            for (i in 0 until absentDaysChoice1!!.size) {
                                absentDaysChoice1Array!!.add(
                                    absentDaysChoice1[i]!!
                                )
                            }
                        }
                        if (choice1.presentDays != null) {
                            ch = 1
                            val presentDaysChoice1 = choice1.presentDays
                            for (i in 0 until presentDaysChoice1.size) {
                                presentDaysChoice1Array!!.add(
                                    presentDaysChoice1[i]!!
                                )
                            }
                        }
                        if (choice1.upcomingDays != null) {
                            ch = 1
                            val upcomingDaysChoice1 = choice1.upcomingDays
                            for (i in 0 until upcomingDaysChoice1.size) {
                                upcomingDaysChoice1Array!!.add(
                                    upcomingDaysChoice1[i]!!
                                )
                            }
                        }
                    } else if (choice1.attending_status
                            .equals("3", ignoreCase = true)
                    ) {
                        if (choice1.absentDays != null) {
                            for (i in 0 until absentDaysChoice1!!.size) {
                                absentDaysChoice1Array!!.add(
                                    absentDaysChoice1[i]!!
                                )
                            }
                            ch = 1
                        }
                        if (choice1.presentDays != null) {
                            val presentDaysChoice1 = choice1.presentDays
                            for (i in 0 until presentDaysChoice1.size) {
                                presentDaysChoice1Array!!.add(
                                    presentDaysChoice1[i]!!
                                )
                            }
                            ch = 1
                        }
                    } else {
                        mCCAModel.choice1 = "0"
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
                    mCCAModel.cca_details_id2 = choice2.cca_details_id.toString()
                    mCCAModel.attending_status2 = choice2.attending_status
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

                    val absentDaysChoice2 = choice2.absentDays
                    absentDaysChoice2Array =
                        java.util.ArrayList<String>()
                    presentDaysChoice2Array =
                        java.util.ArrayList<String>()
                    upcomingDaysChoice2Array =
                        java.util.ArrayList<String>()
                    if (choice2.attending_status.equals("1", ignoreCase = true)) {
                        if (choice2.absentDays != null) {
                            ch = 1
                            for (i in 0 until absentDaysChoice2!!.size) {
                                absentDaysChoice2Array!!.add(
                                    absentDaysChoice2!![i]!!
                                )
                            }
                        }
                        val presentDaysChoice2 = choice2.presentDays
                        if (choice2.presentDays != null) {
                            ch = 1
                            for (i in 0 until presentDaysChoice2!!.size) {
                                presentDaysChoice2Array!!.add(
                                    presentDaysChoice2[i]!!
                                )
                            }
                        }
                        val upcomingDaysChoice2 = choice2.upcomingDays
                        if (choice2.upcomingDays != null) {
                            ch = 1
                            for (i in 0 until upcomingDaysChoice2!!.size) {
                                upcomingDaysChoice2Array!!.add(
                                    upcomingDaysChoice2!![i]!!
                                )
                            }
                        }
                    } else if (choice2.attending_status
                            .equals("3", ignoreCase = true)
                    ) {
                        if (choice2.absentDays != null) {
                            for (i in 0 until absentDaysChoice2!!.size) {
                                absentDaysChoice2Array!!.add(
                                    absentDaysChoice2[i]!!
                                )
                            }
                            ch = 1
                        }
                        if (choice2.presentDays != null) {
                            val presentDaysChoice2 = choice2.presentDays
                            for (i in 0 until presentDaysChoice2!!.size) {
                                presentDaysChoice2Array!!.add(
                                    presentDaysChoice2[i]!!
                                )
                            }
                            ch = 1
                        }
                    } else {
                        mCCAModel.choice2 = "0"
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
                mCCAAttendanceModel.dateAttend =
                    absentDaysChoice1Array!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "a"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (upcomingDaysChoice1Array!!.size > 0) {
            for (i in upcomingDaysChoice1Array!!.indices)  //                datestringChoice1.add(upcomingDaysChoice1Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend =
                    upcomingDaysChoice1Array!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "u"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (presentDaysChoice1Array!!.size > 0) {
            for (i in presentDaysChoice1Array!!.indices)  //                datestringChoice1.add(presentDaysChoice1Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend =
                    presentDaysChoice1Array!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "p"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }
        if (absentDaysChoice2Array!!.size > 0) {
            for (i in absentDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend =
                    absentDaysChoice2Array!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "a"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (upcomingDaysChoice2Array!!.size > 0) {
            for (i in upcomingDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend =
                    upcomingDaysChoice2Array!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "u"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (presentDaysChoice2Array!!.size > 0) {
            for (i in presentDaysChoice2Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend =
                    presentDaysChoice2Array!!!!.get(i).toString()
                mCCAAttendanceModel.statusCCA = "p"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (datestringChoice1!!.size > 0) {
            datestringChoice1!!.sortWith(Comparator { s1, s2 -> s1!!.dateAttend.compareTo(s2!!.dateAttend, ignoreCase = true) })
        }
        if (datestringChoice2!!.size > 0) {
            datestringChoice1!!.sortWith(Comparator { s1, s2 -> s1!!.dateAttend.compareTo(s2!!.dateAttend, ignoreCase = true) })
        }
        mCCAModel.calendarDaysChoice1 = datestringChoice1
        mCCAModel.calendarDaysChoice2 = datestringChoice2
        println("ch:::$ch")

        if (ch == 1) {
            mCCADetailModelArrayList!!.add(mCCAModel)
        }
    }
}