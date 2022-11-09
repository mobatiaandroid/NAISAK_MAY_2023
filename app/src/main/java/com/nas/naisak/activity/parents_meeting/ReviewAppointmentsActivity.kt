package com.nas.naisak.activity.parents_meeting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.parents_meeting.adapter.ReviewAppointmentsRecyclerviewAdapter
import com.nas.naisak.activity.parents_meeting.model.PostSlotRequestModel
import com.nas.naisak.activity.parents_meeting.model.PostSlotResponseModel
import com.nas.naisak.activity.parents_meeting.model.ReviewAppointmentsResponseModel
import com.nas.naisak.activity.parents_meeting.model.ReviewModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.fragment.parents_meeting.model.ConfirmApi
import com.nas.naisak.fragment.parents_meeting.model.PTAConfirmResponseModel
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReviewAppointmentsActivity : AppCompatActivity() {
    var mContext: Context = this
    var recycler_review: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
//    var headermanager: HeaderManager? = null
//    var back: ImageView? = null
//    var home: ImageView? = null
    var confirmTV: TextView? = null
    lateinit var progressBar: ProgressBar
    var reviewArrayList: ArrayList<ReviewModel>? = ArrayList()
    var mVideoRecyclerviewAdapter: ReviewAppointmentsRecyclerviewAdapter? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    var confirmJsonArrayId = ""
    var confirmVisibility = false
    var confirmJsonArrayIds = ""
    var confirmJsonArrayIdSingle = ""
    var confirmBySingle = false
    var list: ArrayList<String>? = ArrayList()
    var lists: ArrayList<String>? = ArrayList()
    var survey_satisfation_status = 0
    var tempReviewList: ArrayList<ReviewAppointmentsResponseModel.Data.Lists> = ArrayList()
    var currentPage = 0
    var backRelative : RelativeLayout? = null
    var currentPageSurvey = 0
    private val surveySize = 0
    var pos = -1
//    var surveyArrayList: ArrayList<SurveyModel>? = null
//    var surveyQuestionArrayList: ArrayList<SurveyQuestionsModel>? = null
//    var surveyAnswersArrayList: ArrayList<SurveyAnswersModel>? = null
//    var mAnswerList: ArrayList<AnswerSubmitModel>? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    private val surveyEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_appointments)
        mContext = this
        confirmBySingle = false
        initialiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            ptaReviewListAPICall()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun ptaReviewListAPICall() {
        reviewArrayList = ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<ReviewAppointmentsResponseModel> =
            ApiClient.getClient.reviewPta("Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<ReviewAppointmentsResponseModel> {
            override fun onResponse(
                call: Call<ReviewAppointmentsResponseModel>,
                response: Response<ReviewAppointmentsResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.lists!!.size > 0 ){
                                for (i in response.body()!!.data!!.lists!!.indices){
                                    tempReviewList.add(response.body()!!.data!!.lists!![i]!!)
                                    val imageDetail  = response.body()!!.data!!.lists!![i]!!
                                    val mPhotosModel = ReviewModel()
                                    mPhotosModel.id = imageDetail.id.toString()
                                    mPhotosModel.studentId = imageDetail.student_id.toString()
                                    mPhotosModel.studentName = imageDetail.student
                                    mPhotosModel.studentPhoto =
                                        imageDetail.student_photo
                                    mPhotosModel.staffId = imageDetail.staff_id.toString()
                                    mPhotosModel.staffName = imageDetail.staff
                                    mPhotosModel.dateAppointment = imageDetail.date
                                    mPhotosModel.startTime = imageDetail.start_time
                                    mPhotosModel.endTime = imageDetail.end_time
                                    mPhotosModel.className = imageDetail.student_class
                                    mPhotosModel.status = imageDetail.status.toString()
                                    mPhotosModel.vpml = imageDetail.vpml
                                    mPhotosModel.room = imageDetail.room
                                    mPhotosModel.book_end_date =
                                        imageDetail.book_end_date
                                    mPhotosModel.booking_open =
                                        imageDetail.booking_open
                                    try {
                                        var dateStart: Date
                                        var dateEnd: Date
                                        val formatter: DateFormat =
                                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        dateStart = formatter.parse(
                                            imageDetail.date + " " + imageDetail.start_time

                                        )
                                        dateEnd = formatter.parse(
                                            imageDetail.date + " " + imageDetail.end_time

                                        )
                                        //Subtracting 6 hours from selected time
                                        val timeStart = dateStart.time
                                        val timeEnd = dateEnd.time
                                        val formatterTime =
                                            SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                                        val mStartTime = formatterTime.format(timeStart)
                                        val mEndTime = formatterTime.format(timeEnd)
                                        mPhotosModel.startTimeFormated = mStartTime
                                        mPhotosModel.endTimeFormated = mEndTime
                                        Log.e("StartTime ", mStartTime)
                                        Log.e("EndTime ", mEndTime)
                                    } catch (e: Exception) {
                                        Log.d("Exception", "" + e)
                                    }
                                    if (imageDetail.status!!.toString().equals(
                                            "2",
                                            ignoreCase = true
                                        ) && imageDetail.booking_open
                                            .equals("y", ignoreCase = true)
                                    ) {
                                        list!!.add(imageDetail.id.toString())
                                    }
                                    reviewArrayList!!.add(mPhotosModel)
                                    if (list!!.size > 0) {
                                        val jsArray = JSONArray(list)
                                        confirmJsonArrayId = jsArray.toString()
                                    }
                                }
                            }else{
                                CommonMethods.showDialogueWithOk(mContext,"No Appointments available","Alert")

                            }
                            for (j in reviewArrayList!!.indices) {
                                if (reviewArrayList!!.get(j).status!!.toString().equals("2", ignoreCase = true) && reviewArrayList!!.get(j)
                                        .booking_open.equals("y", ignoreCase = true)
                                ) {
                                    confirmVisibility = true
                                    break
                                } else {
                                    confirmVisibility = false
                                }
                            }
//                                            mVideoRecyclerviewAdapter = new ReviewAppointmentsRecyclerviewAdapter(mContext, mVideoModelArrayList);
                            //                                            mVideoRecyclerviewAdapter = new ReviewAppointmentsRecyclerviewAdapter(mContext, mVideoModelArrayList);
                            mVideoRecyclerviewAdapter = ReviewAppointmentsRecyclerviewAdapter(mContext,reviewArrayList,
                                { position ->
                                    if (reviewArrayList!!.get(position).getBooking_open()
                                            .equals("y",ignoreCase = true)
                                    ) {
                                        showDialogAlertDoubeBtn(
                                            mContext as Activity,
                                            "Alert",
                                            "Do you want to cancel this appointment?",
                                            R.drawable.questionmark_icon,
                                            R.drawable.round,
                                            "1",
                                            position
                                        )
                                    } else {
                                        CommonMethods.showDialogueWithOk(mContext,"Booking and cancellation date is over","Alert")
                                    }
                                }
                            ) { position ->
                                if (reviewArrayList!!.get(position).getBooking_open()
                                        .equals("y", ignoreCase = true)
                                ) {
                                    confirmBySingle = true
                                    lists = java.util.ArrayList()
                                    for (i in reviewArrayList!!.indices) {
                                        if (reviewArrayList!!.get(i).getId()
                                                .equals(
                                                    reviewArrayList!!.get(position)
                                                        .getId(), ignoreCase = true
                                                )
                                        ) {
                                            lists!!.add(
                                                reviewArrayList!!.get(position).getId()
                                            )
                                            val jsArray = JSONArray(lists)
                                            confirmJsonArrayIdSingle = jsArray.toString()
                                            break
                                        }
                                    }
                                    showDialogAlertDoubeBtn(
                                        mContext as Activity,
                                        "Alert",
                                        "Do you want to confirm this appointment?",
                                        R.drawable.questionmark_icon,
                                        R.drawable.round,
                                        "2",
                                        position
                                    )
                                } else {
                                    CommonMethods.showDialogueWithOk(
                                        mContext,
                                        "Booking and cancellation date is over",
                                        "Alert"
                                    )
                                }
                            }


                            recycler_review!!.adapter = mVideoRecyclerviewAdapter
                            if (confirmVisibility) {
                                confirmTV!!.visibility = View.VISIBLE
                            } else {
                                confirmTV!!.visibility = View.GONE
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

            override fun onFailure(call: Call<ReviewAppointmentsResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })

    }



    fun showDialogAlertDoubeBtn(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int,
        cancel: String,
        mPosition: Int
    ) {
        val dialog = Dialog(activity!!)
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
            if (cancel.equals("1", ignoreCase = true)) {
                postSelectedSlot(mPosition)
            } else {
                confirmApiCall()
            }
            dialog.dismiss()
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun confirmApiCall() {
        confirmJsonArrayIds = if (confirmBySingle) {
            confirmJsonArrayIdSingle
        } else {
            confirmJsonArrayId
        }
//        var bookID=book_id.toString()
//        var slot:String="["+book_id+"]"
        var model= ConfirmApi(confirmJsonArrayIds)
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<PTAConfirmResponseModel> =
            ApiClient.getClient.confirmSlot( model,"Bearer $token")
        progressBar.visibility = View.VISIBLE


        call.enqueue(object : Callback<PTAConfirmResponseModel> {
            override fun onResponse(
                call: Call<PTAConfirmResponseModel>,
                response: Response<PTAConfirmResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status!!.equals(100)){

                            showDialogAlertSingleBtn(
                                mContext as Activity,
                                "Alert",
                                "Successfully confirmed appointment.",
                                R.drawable.tick,
                                R.drawable.round,

                            )

//                                            Toast.makeText(mContext,"Successfully Confirmed.",Toast.LENGTH_SHORT).show();//rijo

//                                            Toast.makeText(mContext,"Successfully Confirmed.",Toast.LENGTH_SHORT).show();//rijo
                            ptaReviewListAPICall()

                        }
                        else if (response.body()!!.status!!.equals(109))
                        {

                            showDialogAlertSingleBtn(
                                mContext as Activity,
                                "Alert",
                                "Request cancelled successfully",
                                R.drawable.tick,
                                R.drawable.round
                            )
                            ptaReviewListAPICall()
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

            override fun onFailure(call: Call<PTAConfirmResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE

                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })
    }

    fun showDialogAlertSingleBtn(
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
        }
        //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show()
    }

    private fun postSelectedSlot(position: Int) {
//hardcoded
            val body = PostSlotRequestModel(reviewArrayList!!.get(position).getStudentId(),
                tempReviewList!![position].pta_time_slot_id.toString())
            val token = PreferenceManager.getUserCode(mContext)
            val call: Call<PostSlotResponseModel> =
                ApiClient.getClient.postSlot( body,"Bearer $token")
        progressBar.visibility = View.VISIBLE
            call.enqueue(object : Callback<PostSlotResponseModel> {
                override fun onResponse(
                    call: Call<PostSlotResponseModel>,
                    response: Response<PostSlotResponseModel>
                ) {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                                val statusCode = response.body()!!.status

                                if (statusCode == 100) {
                                showDialogAlertSingleBtn(
                                    mContext as Activity,
                                    "Alert",
                                    "Reserved Only – Please review and confirm bookingReserved Only – Please review and confirm booking",
                                    R.drawable.tick,
                                    R.drawable.round,

                                )
                                ptaReviewListAPICall()
                            } else if (statusCode == 109) {
                                showDialogAlertSingleBtn(
                                    mContext as Activity,
                                    "Alert",
                                    "Request cancelled successfully.",
                                    R.drawable.tick,
                                    R.drawable.round,

                                )
                                    ptaReviewListAPICall()
                            } else if (statusCode == 310) {
                                showDialogAlertSingleBtn(
                                    mContext as Activity,
                                    "Alert",
                                    "Slot is already booked by an another user.",
                                    R.drawable.exclamationicon,
                                    R.drawable.round,

                                )
                            } else if (statusCode == 315) {
                                showDialogAlertSingleBtn(
                                    mContext as Activity,
                                    "Alert",
                                    "Slot not found.",
                                    R.drawable.exclamationicon,
                                    R.drawable.round,

                                )
                            } else if (statusCode == 316) {
                                showDialogAlertSingleBtn(
                                    mContext as Activity,
                                    "Alert",
                                    getString(R.string.datexpirecontact),
                                    R.drawable.exclamationicon,
                                    R.drawable.round,

                                )
                            } else {
                                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
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
                override fun onFailure(call: Call<PostSlotResponseModel>, t: Throwable) {
                    progressBar.visibility = View.GONE

                    CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                }

            })




    }




    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        confirmTV = findViewById<View>(R.id.confirmTV) as TextView
        progressBar = findViewById(R.id.progress)
        backRelative = findViewById(R.id.backRelative)
        backRelative!!.setOnClickListener {
            finish()
        }
//        headermanager =
//            HeaderManager(this@ReviewAppointmentsRecyclerViewActivity, "Review Appointments")
//        headermanager.getHeader(relativeHeader, 1)
//        back = headermanager.getLeftButton()
//        headermanager.setButtonLeftSelector(
//            R.drawable.back,
//            R.drawable.back
//        )
//        back!!.setOnClickListener { finish() }
////        home = headermanager.getLogoButton()
//        home!!.setOnClickListener {
//            val `in` = Intent(mContext, HomeActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }
        recycler_review = findViewById<View>(R.id.recycler_review) as RecyclerView

        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        val spacing = 10 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)

        //or

        //or
        val divider = DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        recycler_review!!.addItemDecoration(divider)

        recycler_review!!.addItemDecoration(itemDecoration)
        recycler_review!!.setLayoutManager(recyclerViewLayoutManager)
        confirmTV!!.setOnClickListener {
            if (CommonMethods.isInternetAvailable(mContext)) {
                confirmApiCall()
            } else {
                CommonMethods.showSuccessInternetAlert(mContext)
            }
        }
    }
}
