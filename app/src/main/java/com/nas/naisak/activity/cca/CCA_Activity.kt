package com.nas.naisak.activity.cca

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAsListActivityAdapter
import com.nas.naisak.activity.cca.adapter.StrudentSpinnerAdapter
import com.nas.naisak.activity.cca.model.*
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.commonmodels.StudentListReponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CCA_Activity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progress: ProgressBar
    var studentsModelArrayList: java.util.ArrayList<StudentListReponseModel.Data.Lists>? = ArrayList()
    var mCCAmodelArrayList: java.util.ArrayList<CCAModel>? = ArrayList()
    var CCADetailModelArrayList: java.util.ArrayList<CCADetailModel>? = ArrayList()
    var CCAchoiceModelArrayList: java.util.ArrayList<CCAchoiceModel>? = ArrayList()
    var CCAchoiceModelArrayList2: java.util.ArrayList<CCAchoiceModel>? = ArrayList()
    var studentName: TextView? = null
    var textViewYear: TextView? = null
    var enterTextView: TextView? = null
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var mStudentSpinner: LinearLayout? = null
    var relativeHeader: RelativeLayout? = null

    var studImg: ImageView? = null
    var stud_img = ""

    var tab_type = "CCA Options"
    var extras: Bundle? = null
    var recycler_review: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var mCCAsActivityAdapter: CCAsListActivityAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cca)
        mContext = this
        initilaiseUI()
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        mStudentSpinner!!.setOnClickListener { showStudentsList(studentsModelArrayList) }
        if (CommonMethods.isInternetAvailable(mContext)) {
            getStudentList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun showStudentsList(mStudentArray: java.util.ArrayList<StudentListReponseModel.Data.Lists>?) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button))
        } else {
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button)
        }

        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        socialMediaList!!.addItemDecoration(divider)

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val studentAdapter = StrudentSpinnerAdapter(mContext, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                dialog.dismiss()
                studentName!!.setText(mStudentArray!!.get(position).name)
                stud_id = mStudentArray!!.get(position).id.toString()
                stud_name = mStudentArray.get(position).name.toString()
                stud_class = mStudentArray.get(position).student_class.toString()
                stud_img = mStudentArray.get(position).photo.toString()
                textViewYear!!.text = "Class : " + mStudentArray.get(position).student_class
                if (stud_img != "") {
                    Glide.with(mContext).load(CommonMethods.replace(stud_img)).fitCenter()

                        .placeholder(R.drawable.boy).into(studImg!!)
                } else {
                    studImg!!.setImageResource(R.drawable.boy)
                }
                PreferenceManager.setCCAStudentIdPosition(
                    mContext,
                    position.toString() + ""
                )
                getCCAListAPI(stud_id)
            }

        })

        dialog.show()
    }

    private fun getStudentList() {
        studentsModelArrayList = ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<StudentListReponseModel> =
            ApiClient.getClient.getStudent( "Bearer $token")
        progress.visibility = View.VISIBLE
        call.enqueue(object : Callback<StudentListReponseModel> {
            override fun onResponse(
                call: Call<StudentListReponseModel>,
                response: Response<StudentListReponseModel>
            ) {
                progress.visibility = View.GONE
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.lists!!.size > 0 ){

                                for (i in response.body()!!.data!!.lists!!.indices){
                                    studentsModelArrayList!!.add(response.body()!!.data!!.lists!![i]!!)
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext)
                                        .equals("")
                                ) {
                                    studentName!!.text = studentsModelArrayList!![0].name
                                    stud_id = studentsModelArrayList!![0].id.toString()
                                    stud_name = studentsModelArrayList!![0].name.toString()
                                    stud_class = studentsModelArrayList!![0].student_class.toString()
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![0].student_class
                                    stud_img = studentsModelArrayList!![0].photo.toString()
                                    if (stud_img != "") {
                                        Glide.with(mContext).load(CommonMethods.replace(stud_img)).fitCenter()
                                            .placeholder(R.drawable.boy).into(studImg!!)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0")
                                } else {
                                    val studentSelectPosition = Integer.valueOf(
                                        PreferenceManager.getCCAStudentIdPosition(mContext)
                                    )
                                    studentName!!.text = studentsModelArrayList!![studentSelectPosition].name
                                    stud_id =
                                        studentsModelArrayList!![studentSelectPosition].id.toString()
                                    stud_name =
                                        studentsModelArrayList!![studentSelectPosition].name.toString()
                                    stud_class =
                                        studentsModelArrayList!![studentSelectPosition].student_class.toString()
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![studentSelectPosition].student_class.toString()
                                    stud_img =
                                        studentsModelArrayList!![studentSelectPosition].photo.toString()
                                    if (stud_img != "") {
                                        Glide.with(mContext).load(CommonMethods.replace(stud_img)).fitCenter()

                                            .placeholder(R.drawable.boy).into(studImg!!)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                }
                                getCCAListAPI(stud_id)
                            }else{

                                //CustomStatusDialog();
                                Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT)
                                    .show()
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

            override fun onFailure(call: Call<StudentListReponseModel>, t: Throwable) {
                progress.visibility = View.GONE
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })
    }

    private fun getCCAListAPI(studId: String) {
        val body = CCAListRequestModel(studId)
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<CCAListResponseModel> =
            ApiClient.getClient.getCCAList( body,"Bearer $token")
        progress.visibility = View.VISIBLE
        call.enqueue(object : Callback<CCAListResponseModel> {
            override fun onResponse(
                call: Call<CCAListResponseModel>,
                response: Response<CCAListResponseModel>
            ) {
                progress.visibility = View.GONE
                    mCCAmodelArrayList = java.util.ArrayList()

                    if (response.body() != null) {
                        if (response.body()!!.status!!.equals(100)) {

                            if (response.body()!!.data!!.lists!!.isNotEmpty()) {
                                for (i in response.body()!!.data!!.lists!!.indices) {
                                    enterTextView!!.visibility = View.VISIBLE

                                    mCCAmodelArrayList!!.add(addCCAlist(response.body()!!.data!!.lists!![i]))
                                }
                                Log.e("arraty",mCCAmodelArrayList.toString())
                                if (mCCAmodelArrayList!!.size > 0) {
                                    mCCAsActivityAdapter = CCAsListActivityAdapter(
                                        this@CCA_Activity,
                                        mCCAmodelArrayList
                                    )
                                    recycler_review!!.adapter = mCCAsActivityAdapter
                                }
                            } else {
                                mCCAsActivityAdapter =
                                    CCAsListActivityAdapter(this@CCA_Activity, mCCAmodelArrayList)
                                recycler_review!!.adapter = mCCAsActivityAdapter
                                enterTextView!!.visibility = View.GONE
                                Toast.makeText(
                                    this@CCA_Activity,
                                    "No CCA available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{
                            Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show()
                        }
                    }
//                    else if (response_code.equals("500", ignoreCase = true)) {
//                        AppUtils.showDialogAlertDismiss(
//                            mContext as Activity,
//                            "Alert",
//                            getString(R.string.common_error),
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    } else if (response_code.equals("400", ignoreCase = true)) {
//                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
//                            fun tokenrenewed() {
//                                getCCAListAPI(studentId)
//                            }
//                        })
//                    } else if (response_code.equals("401", ignoreCase = true)) {
//                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
//                            fun tokenrenewed() {
//                                getCCAListAPI(studentId)
//                            }
//                        })
//                    } else if (response_code.equals("402", ignoreCase = true)) {
//                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
//                            fun tokenrenewed() {
//                                getCCAListAPI(studentId)
//                            }
//                        })
//                    }
                    else {
                        CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                    }

            }

            override fun onFailure(call: Call<CCAListResponseModel>, t: Throwable) {
                progress.visibility = View.GONE
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })
    }

    private fun addCCAlist(dataObject: CCAListResponseModel.Data.Lists?): CCAModel {
        val mCCAModel = CCAModel()
        mCCAModel.cca_days_id = dataObject!!.cca_days_id.toString()
        mCCAModel.title = dataObject.title
        mCCAModel.from_date = dataObject.from_date
        mCCAModel.to_date = dataObject.to_date
        mCCAModel.isAttendee = dataObject.isAttendee
        mCCAModel.status = dataObject.status.toString()

        mCCAModel.submission_dateTime = dataObject.submission_dateTime
        mCCAModel.isSubmissionDateOver = dataObject.isSubmissiondateOver
        val jsonCCADetailArray: ArrayList<CCAListResponseModel.Data.Lists.Detail?>? = dataObject.details
        Log.e("choice2adasda", dataObject.details!![0]!!.choice2.toString())

        Log.e("choice2adasda", dataObject.details!![0]!!.choice2!![0]!!.attending_status.toString())
        CCADetailModelArrayList = java.util.ArrayList<CCADetailModel>()
        if (jsonCCADetailArray!!.isNotEmpty()) {
            for (element in jsonCCADetailArray) {
                val objectCCA = element
                val mCCADetailModel = CCADetailModel()
                mCCADetailModel.day = objectCCA!!.day

                val jsonCCAChoiceArray = objectCCA.choice1
                val jsonCCAChoiceArray2 = objectCCA.choice2
                Log.e("choice1",objectCCA.choice1.toString())
                Log.e("choice2 atte", objectCCA.choice1!![0]!!.attending_status.toString())
                Log.e("choice2 atte", objectCCA.choice2!![0]!!.attending_status.toString())
                Log.e("choice2 atte", jsonCCAChoiceArray2!![0]!!.attending_status.toString())
                CCAchoiceModelArrayList = java.util.ArrayList<CCAchoiceModel>()
                if (jsonCCAChoiceArray!!.size > 0) {
                    var k = 0
                    for (j in 0..jsonCCAChoiceArray.size) {
                        val mCCADetailModelchoice = CCAchoiceModel()
                        if (jsonCCAChoiceArray.size != j) {
                            val objectCCAchoice = jsonCCAChoiceArray[j]
                            mCCADetailModelchoice.cca_item_name = objectCCAchoice!!.cca_item_name
                            mCCADetailModelchoice.cca_details_id = objectCCAchoice.cca_details_id.toString()
                            mCCADetailModelchoice.isattending = objectCCAchoice.isAttendee
                            mCCADetailModelchoice.cca_item_start_time = objectCCAchoice.cca_item_start_time
                            mCCADetailModelchoice.cca_item_end_time = objectCCAchoice.cca_item_end_time
                            mCCADetailModelchoice.venue = objectCCAchoice.venue

                            if (objectCCAchoice.attending_status
                                    .equals("0", ignoreCase = true)
                            ) {
                                if (dataObject.isAttendee
                                        .equals("2", ignoreCase = true)
                                ) {
                                    mCCADetailModelchoice.status = "1"
                                    mCCADetailModel.choice1 = objectCCAchoice.cca_item_name
                                    mCCADetailModel.choice1Id = objectCCAchoice.cca_details_id.toString()
                                } else {
                                    mCCADetailModelchoice.status = "0"
                                }
                                k = k + 1
                            } else {
                                mCCADetailModelchoice.status = "0"
                            }
                            mCCADetailModelchoice.disableCccaiem = false
                            mCCADetailModelchoice.dayChoice = objectCCAchoice.day
                        } else {
                            mCCADetailModelchoice.cca_item_name = "None"
                            mCCADetailModelchoice.cca_details_id = "-541"
                            mCCADetailModelchoice.venue = "0"
                            mCCADetailModelchoice.isattending = "0"
                            if (k == 0) {
                                if (dataObject.isAttendee
                                        .equals("2", ignoreCase = true)
                                ) {
                                    mCCADetailModelchoice.status = "1"
                                    mCCADetailModel.choice1 = "None"
                                    mCCADetailModel.choice1Id = "-541"
                                } else {
                                    mCCADetailModelchoice.status = "0"
                                }
                            } else {
                                mCCADetailModelchoice.status = "0"
                            }
                            mCCADetailModelchoice.disableCccaiem = false
                            mCCADetailModelchoice.dayChoice = objectCCA.day
                        }
                        CCAchoiceModelArrayList!!.add(mCCADetailModelchoice)
                    }
                }
                mCCADetailModel.ccaChoiceModel = CCAchoiceModelArrayList
                CCAchoiceModelArrayList2 = java.util.ArrayList<CCAchoiceModel>()
                if (jsonCCAChoiceArray2!!.isNotEmpty()) {
                    var k = 0
                    for (j in 0..jsonCCAChoiceArray2.size) {
                        val mCCADetailModelchoice = CCAchoiceModel()
                        if (jsonCCAChoiceArray2.size != j) {
                            val objectCCAchoice = jsonCCAChoiceArray2[j]
                            mCCADetailModelchoice.cca_item_name = objectCCAchoice!!.cca_item_name
                            mCCADetailModelchoice.cca_details_id = objectCCAchoice.cca_details_id.toString()
                            mCCADetailModelchoice.isattending = objectCCAchoice.isAttendee
                            mCCADetailModelchoice.cca_item_start_time = objectCCAchoice.cca_item_start_time
                            mCCADetailModelchoice.cca_item_end_time = objectCCAchoice.cca_item_end_time
                            mCCADetailModelchoice.venue = objectCCAchoice.venue
                            mCCADetailModelchoice.dayChoice = objectCCAchoice.day
                            if (objectCCAchoice.attending_status
                                    .equals("0", ignoreCase = true)
                            ) {
                                if (dataObject.isAttendee
                                        .equals("2", ignoreCase = true)
                                ) {
                                    mCCADetailModelchoice.status = "1"
                                    mCCADetailModel.choice2 = objectCCAchoice.cca_item_name
                                    mCCADetailModel.choice2Id = objectCCAchoice.cca_details_id.toString()
                                    Log.e("choice2qqq",mCCADetailModel.choice2.toString())
                                } else {
                                    mCCADetailModelchoice.status = "0"
                                }
                                k += 1
                            } else {
                                mCCADetailModelchoice.status = "0"
                            }
                            mCCADetailModelchoice.disableCccaiem = false
                        } else {
                            mCCADetailModelchoice.cca_item_name = "None"
                            mCCADetailModelchoice.cca_details_id = "-541"
                            mCCADetailModelchoice.isattending = "0"
                            mCCADetailModelchoice.venue = "0"
                            if (k == 0) {
                                if (dataObject.isAttendee
                                        .equals("2", ignoreCase = true)
                                ) {
                                    mCCADetailModelchoice.status = "1"
                                    mCCADetailModel.choice2 = "None"
                                    mCCADetailModel.choice2Id = "-541"
                                } else {
                                    mCCADetailModelchoice.status = "0"
                                }
                            } else {
                                mCCADetailModelchoice.status = "0"
                            }
                            mCCADetailModelchoice.dayChoice = objectCCA.day
                            mCCADetailModelchoice.disableCccaiem = false
                        }
                        CCAchoiceModelArrayList2!!.add(mCCADetailModelchoice)
                    }
                }
                mCCADetailModel.ccaChoiceModel2 = CCAchoiceModelArrayList2
                CCADetailModelArrayList!!.add(mCCADetailModel)
            }
        }
        mCCAModel.details = CCADetailModelArrayList
        return mCCAModel
    }

    private fun initilaiseUI() {
        progress = findViewById(R.id.progress)
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        val spacing = 5 // 50px

        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        recycler_review!!.addItemDecoration(divider)
        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
//        recycler_review!!.addItemDecoration(
//            DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider))
//        )
        recycler_review!!.addItemDecoration(itemDecoration)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
//        headermanager = HeaderManager(this@CCA_Activity, tab_type)
//        headermanager.getHeader(relativeHeader, 0)
//        back = headermanager.getLeftButton()
//        headermanager.setButtonLeftSelector(
//            R.drawable.back,
//            R.drawable.back
//        )
//        back.setOnClickListener {
//            AppUtils.hideKeyBoard(mContext)
//            finish()
//        }
//        home = headermanager.getLogoButton()
//        home.setOnClickListener(View.OnClickListener {
//            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        })
        studImg = findViewById(R.id.imagicon)

        mStudentSpinner = findViewById<View>(R.id.studentSpinner) as LinearLayout
        studentName = findViewById<View>(R.id.studentName) as TextView
        enterTextView = findViewById<View>(R.id.enterTextView) as TextView
        textViewYear = findViewById<View>(R.id.textViewYear) as TextView
        recycler_review!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (mCCAmodelArrayList!![position].status.equals("0") || mCCAmodelArrayList!![position].status.equals(
                        "2"
                    )
                ) {
                    callStatusChangeApi(
                        mCCAmodelArrayList!![position].cca_days_id,
                        position,
                        mCCAmodelArrayList!![position].status
                    )
                }
                if (mCCAmodelArrayList!![position].isAttendee.equals("0")) {
                    if (mCCAmodelArrayList!![position].isSubmissionDateOver.equals(
                            "0"
                        )
                    ) {
                        if (mCCAmodelArrayList!![position].details.size > 0) {
                            val intent = Intent(mContext, CCASelectionActivity::class.java)
                            intent.putExtra(
                                "CCA_Detail",
                                mCCAmodelArrayList!![position].details
                            )
                            intent.putExtra("tab_type", tab_type)
                            PreferenceManager.setStudIdForCCA(mContext, stud_id)
                            PreferenceManager.setStudNameForCCA(mContext, stud_name)
                            PreferenceManager.setStudClassForCCA(mContext, stud_class)
                            PreferenceManager.setCCATitle(
                                mContext,
                                mCCAmodelArrayList!![position].title
                            )
                            PreferenceManager.setCCAItemId(
                                mContext,
                                mCCAmodelArrayList!![position].cca_days_id
                            )
                            startActivity(intent)
                        } else {
                            CommonMethods.showDialogueWithOk(mContext,"No Data Available","Alert")
                        }
                    } else {
                        CommonMethods.showDialogueWithOk(mContext,"CCA Sign-Up Closed","Alert")
                        
                    }
                } else if (mCCAmodelArrayList!![position].isAttendee.equals("2")) {
                    val intent =
                        Intent(mContext, CCAsReviewEditAfterSubmissionActivity::class.java)
                    Log.e("cca choice1s",mCCAmodelArrayList!![position].details.get(0).choice1)
                    Log.e("cca choice2s",mCCAmodelArrayList!![position].details.get(0).choice2)
                    intent.putExtra("tab_type", tab_type)
                    intent.putExtra("CCA_Detail", mCCAmodelArrayList!![position].details)
                    intent.putExtra(
                        "submissiondateover",
                        mCCAmodelArrayList!![position].isSubmissionDateOver
                    )
                    PreferenceManager.setStudIdForCCA(mContext, stud_id)
                    PreferenceManager.setStudNameForCCA(mContext, stud_name)
                    PreferenceManager.setStudClassForCCA(mContext, stud_class)
                    PreferenceManager.setCCATitle(
                        mContext,
                        mCCAmodelArrayList!![position].title
                    )
                    PreferenceManager.setCCAItemId(
                        mContext,
                        mCCAmodelArrayList!![position].cca_days_id
                    )
                    startActivity(intent)
                } else {
                    val intent =
                        Intent(mContext, CCAsReviewAfterSubmissionActivity::class.java)
                    intent.putExtra("tab_type", tab_type)
                    PreferenceManager.setStudIdForCCA(mContext, stud_id)
                    PreferenceManager.setStudNameForCCA(mContext, stud_name)
                    PreferenceManager.setStudClassForCCA(mContext, stud_class)
                    PreferenceManager.setCCATitle(
                        mContext,
                        mCCAmodelArrayList!![position].title
                    )
                    PreferenceManager.setCCAItemId(
                        mContext,
                        mCCAmodelArrayList!![position].cca_days_id
                    )
                    startActivity(intent)
                }
            }

        })
    }

    private fun callStatusChangeApi(ccaDaysId: String?, position: Int, status: String?) {

    }
}