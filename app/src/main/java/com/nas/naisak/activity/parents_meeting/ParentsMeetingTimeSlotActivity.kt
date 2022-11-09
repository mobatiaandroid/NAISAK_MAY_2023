package com.nas.naisak.activity.parents_meeting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.parents_meeting.adapter.ParentsEveningRoomListAdapter
import com.nas.naisak.activity.parents_meeting.adapter.ParentsEveningTimeSlotRecyclerviewAdapter
import com.nas.naisak.activity.parents_meeting.model.PostSlotRequestModel
import com.nas.naisak.activity.parents_meeting.model.PostSlotResponseModel
import com.nas.naisak.calender.model.CalendarEventsModel
import com.nas.naisak.commonmodels.TimeSlotsRequestModel
import com.nas.naisak.commonmodels.TimeSlotsResponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.parents_meeting.model.ConfirmApi
import com.nas.naisak.fragment.parents_meeting.model.PTAConfirmResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ParentsMeetingTimeSlotActivity : AppCompatActivity() {
    var mContext: Context = this
    var relativeHeader: RelativeLayout? = null
    lateinit var infoRoomImg:ImageView
    var mListViewArray: ArrayList<CalendarEventsModel> = ArrayList<CalendarEventsModel>()
    var mListViewArrayPost: ArrayList<CalendarEventsModel>? = null

    var extras: Bundle? = null
    var mStaffid = ""
    var slotId = ""
    var cal: Calendar? = null
    var studentNameTV: TextView? = null
    var studentClassTV: TextView? = null
    var cancelTextView: TextView? = null
    var reviewConfirmTextView: TextView? = null
    var dateTextView: TextView? = null
    var staffTV: TextView? = null
    var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    var recycler_view_alloted_time: RecyclerView? = null
    private var mStudentId = ""
    private var mStudentName = ""
    private var mStaffName = ""
    private var mClass = ""
    private var mDay = 0
    private var mMonth = 0
    private var mMonthString = ""
    private var mYear = 0
    private var selectedDate = ""
    var alreadyslotBookedByUser = false
    var confirmedslotBookedByUser = false
    var confirmedLink = ""
    var dateTextValue: String? = null
    var vpmlBtn: Button? = null

    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_meeting_time_slot)
        mContext = this
        initUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            getPtaAllottedDateList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)

        }
    }

    private fun getPtaAllottedDateList() {
        mListViewArray = ArrayList()
        mListViewArrayPost = ArrayList()
        alreadyslotBookedByUser = false
        confirmedslotBookedByUser = false
        confirmedLink = ""
        var mParentsEveningTimeSlotRecyclerviewAdapter =
            ParentsEveningTimeSlotRecyclerviewAdapter(
                mContext,
                mListViewArray
            )
        recycler_view_alloted_time!!.adapter =
            mParentsEveningTimeSlotRecyclerviewAdapter
        val body = TimeSlotsRequestModel(mStudentId,mStaffid,selectedDate)
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<TimeSlotsResponseModel> =
            ApiClient.getClient.getPtaTimeSlots(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<TimeSlotsResponseModel> {
            override fun onResponse(
                call: Call<TimeSlotsResponseModel>,
                response: Response<TimeSlotsResponseModel>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.lists!!.size > 0 ){
                                for(i in response.body()!!.data!!.lists!!.indices){
                                    mListViewArray.add(getSearchValues(response.body()!!.data!!.lists!![i]!!))
                                }
                                mParentsEveningTimeSlotRecyclerviewAdapter =
                                    ParentsEveningTimeSlotRecyclerviewAdapter(
                                        mContext,
                                        mListViewArray
                                    )
                                recycler_view_alloted_time!!.adapter =
                                    mParentsEveningTimeSlotRecyclerviewAdapter
                                for (j in mListViewArray.indices) {
                                    if (mListViewArray[j].status.equals("2")) {
                                        mListViewArrayPost = ArrayList()
                                        mListViewArrayPost!!.add(mListViewArray[j])
                                        alreadyslotBookedByUser = true
                                        break
                                    }
                                }
                                for (j in mListViewArray.indices) {
                                    if (mListViewArray[j].status.equals("3")) {
                                        confirmedslotBookedByUser = true
                                        confirmedLink = mListViewArray[j].vpml
                                        break
                                    }
                                }
                                if (confirmedslotBookedByUser) {
                                    if (confirmedLink.equals("", ignoreCase = true)) {
                                        vpmlBtn!!.visibility = View.GONE
                                    } else {
                                        vpmlBtn!!.visibility = View.GONE
                                    }
                                    cancelTextView!!.visibility = View.VISIBLE
                                    reviewConfirmTextView!!.visibility = View.INVISIBLE
                                } else if (alreadyslotBookedByUser) {
                                    cancelTextView!!.visibility = View.VISIBLE
                                    reviewConfirmTextView!!.visibility = View.INVISIBLE
                                } else {
                                    cancelTextView!!.visibility = View.INVISIBLE
                                    reviewConfirmTextView!!.visibility = View.INVISIBLE
                                }

                            }else{

                                //CustomStatusDialog();
                                Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }else if(response.body()!!.status!!.equals("310")){
                            showDialogAlertSingleBtn(
                                mContext as Activity,
                                "Alert",
                                "Slot is already booked by an another user.",
                                R.drawable.tick,
                                R.drawable.round
                            )
                        }else{

                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                    }
                }else{
                    CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<TimeSlotsResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })

    }

    private fun getSearchValues(eventdate: TimeSlotsResponseModel.Data.Lists): CalendarEventsModel {
        val mCalendarEventsModel = CalendarEventsModel()
        mCalendarEventsModel.seteventdate(eventdate.date)
        mCalendarEventsModel.eventStartDate = eventdate.start_time
        mCalendarEventsModel.eventEndDate = eventdate.end_time
        mCalendarEventsModel.status = eventdate.status.toString()
        mCalendarEventsModel.book_end_date = eventdate.book_end_date
        mCalendarEventsModel.booking_open = eventdate.booking_open
        mCalendarEventsModel.room = eventdate.room
        mCalendarEventsModel.vpml = eventdate.vpml
        mCalendarEventsModel.setId(eventdate.id!!)
        val dateString = mCalendarEventsModel.geteventdate()
        println("dateString|$dateString|")
        if (dateString != null) {
            val format = SimpleDateFormat(
                "yyyy-MM-dd", Locale.ENGLISH
            )
            try {
                val date = format.parse(dateString)
                val cal1 = Calendar.getInstance()
                cal1.time = date
                println(
                    "date:|" + cal1[Calendar.YEAR] + "|"
                            + "Calender:|" + cal!![Calendar.YEAR] + "|"
                )
                mCalendarEventsModel.day = cal1[Calendar.DAY_OF_MONTH]
                mCalendarEventsModel.year = cal1[Calendar.YEAR]
                mCalendarEventsModel.month = cal1[Calendar.MONTH]
                mCalendarEventsModel.monthString = getMonth(cal1[Calendar.MONTH])
            } catch (e: ParseException) {
                e.localizedMessage
            }
        }
        val format1: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        try {
            val dateStart = format1.parse(mCalendarEventsModel.eventStartDate)
            val format2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val startTime = format2.format(dateStart)
            mCalendarEventsModel.startTime = startTime
            val dateEndTime = format1.parse(mCalendarEventsModel.eventEndDate)
            val endTime = format2.format(dateEndTime)
            mCalendarEventsModel.endTime = endTime
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return mCalendarEventsModel
    }

    private fun initUI() {
        cal = Calendar.getInstance()
        extras = intent.extras
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
        progressBar = findViewById(R.id.progress)

        if (extras != null) {
            mStaffid = extras!!.getString("staff_id")!!
            mStudentId = extras!!.getString("student_id")!!
            mStudentName = extras!!.getString("studentName")!!
            mStaffName = extras!!.getString("staffName")!!
            mClass = extras!!.getString("studentClass")!!
            selectedDate = extras!!.getString("selectedDate")!!
            if (!selectedDate.equals("", ignoreCase = true)) {
                val format = SimpleDateFormat(
                    "yyyy-MM-dd", Locale.ENGLISH
                )
                try {
                    val date = format.parse(selectedDate)
                    val cal1 = Calendar.getInstance()
                    cal1.time = date
                    mDay = cal1[Calendar.DAY_OF_MONTH]
                    mYear = cal1[Calendar.YEAR]
                    mMonth = cal1[Calendar.MONTH]
                    mMonthString = getMonth(cal1[Calendar.MONTH])
                } catch (e: ParseException) {
                    e.localizedMessage
                }
            }
        }

        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        studentNameTV = findViewById<View>(R.id.studentNameTV) as TextView
        studentClassTV = findViewById<View>(R.id.studentClassTV) as TextView
        staffTV = findViewById<View>(R.id.staffTV) as TextView
        cancelTextView = findViewById<View>(R.id.cancelTextView) as TextView
        reviewConfirmTextView = findViewById<View>(R.id.reviewConfirmTextView) as TextView
        dateTextView = findViewById<View>(R.id.dateTextView) as TextView
        vpmlBtn = findViewById<View>(R.id.vpmlBtn) as Button
        infoRoomImg = findViewById<View>(R.id.infoRoomImg) as ImageView
        recycler_view_alloted_time =
            findViewById<View>(R.id.recycler_view_alloted_time) as RecyclerView
        staffTV!!.text = mStaffName
        studentClassTV!!.text = mClass
        studentNameTV!!.text = mStudentName
        dateTextValue = "$mDay $mMonthString $mYear"
        dateTextView!!.text = dateTextValue

        reviewConfirmTextView!!.setOnClickListener {
            showDialogAlertReview(
                mContext as Activity,
                "Alert",
                "Reserved Only - Please review and confirm booking.",
                R.drawable.exclamationicon,
                R.drawable.round
            )
        }
        vpmlBtn!!.setOnClickListener {
            println("ConfirmedLink$confirmedLink")
            if (confirmedLink.equals("", ignoreCase = true)) {
            } else {
                val viewIntent = Intent(
                    "android.intent.action.VIEW",
                    Uri.parse(confirmedLink)
                )
                startActivity(viewIntent)
            }
        }

        PreferenceManager.setBackParent(mContext, false)

        infoRoomImg!!.setOnClickListener {
            if (CommonMethods.isInternetAvailable(mContext)) {
                showRoomList()
            } else {
                CommonMethods.showSuccessInternetAlert(mContext)
            }
        }

        cancelTextView!!.setOnClickListener {

//            if (mListViewArrayPost!![0].booking_open.equals("y",ignoreCase = true)) {
                showDialogAlertDoubleBtn(
                    mContext as Activity,
                    "Confirm",
                    "Do you want to cancel this appointment?",
                    R.drawable.questionmark_icon,
                    R.drawable.round
                )
//            }
//            else {
//                CommonMethods.showDialogueWithOk(mContext,getString(R.string.datexpirecontact),"Alert")
//            }
        }

        recycler_view_alloted_time!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 4)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recycler_view_alloted_time!!.addItemDecoration(itemDecoration)
        recycler_view_alloted_time!!.layoutManager = recyclerViewLayoutManager

        recycler_view_alloted_time!!.addOnItemClickListener(object :OnItemClickListener {

            override fun onItemClicked(position: Int, view: View) {
                if (mListViewArray[position].status.equals("1",ignoreCase = true)) {
                    showDialogAlertSingleBtn(
                        mContext as Activity,
                        "Alert",
                        "This slot is not available.",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                } else if (confirmedslotBookedByUser) {
                    showDialogAlertSingleBtn(
                        mContext as Activity,
                        "Alert",
                        "Your time slot is already confirmed.",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                } else if (!alreadyslotBookedByUser) {
                    mListViewArrayPost = ArrayList()
                    mListViewArrayPost!!.add(mListViewArray[position])
                    if (mListViewArrayPost!![0].booking_open.equals("y",ignoreCase = true)) {
                        showDialogAlertDoubleBtn(
                            mContext as Activity,
                            "Alert",
                            "Do you want to confirm your appointment on " + dateTextValue + "," + mListViewArrayPost!![0].startTime + " - " + mListViewArrayPost!![0].endTime + "?",
                            R.drawable.questionmark_icon,
                            R.drawable.round
                        )
                    } else {
                        CommonMethods.showDialogueWithOk(mContext, getString(R.string.datexpirecontact),"Alert")
                    }
                } else {
                    if (mListViewArray[position].status.equals("2",ignoreCase = true)) {
                        showDialogAlertSingleBtn(
                            mContext as Activity,
                            "Alert",
                            "This slot is reserved by you for the Parents' Evening. Click 'Cancel' option to cancel this appointment",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        showDialogAlertSingleBtn(
                            mContext as Activity,
                            "Alert",
                            "Another Slot is already booked by you. If you want to take appointment on this time, please cancel earlier appointment and try",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }
            }
        })

    }

    private fun showRoomList() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_room_slot_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())


        //if(mSocialMediaArray.get())
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        socialMediaList!!.addItemDecoration(divider)

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsEveningRoomListAdapter(mContext, mListViewArray)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun getMonth(month: Int): String {
        var monthOfYear = ""
        when (month) {
            0 -> monthOfYear = mContext.resources.getString(R.string.january)
            1 -> monthOfYear = mContext.resources.getString(R.string.february)
            2 -> monthOfYear = mContext.resources.getString(R.string.march)
            3 -> monthOfYear = mContext.resources.getString(R.string.april)
            4 -> monthOfYear = mContext.resources.getString(R.string.may)
            5 -> monthOfYear = mContext.resources.getString(R.string.june)
            6 -> monthOfYear = mContext.resources.getString(R.string.july)
            7 -> monthOfYear = mContext.resources.getString(R.string.august)
            8 -> monthOfYear = mContext.resources.getString(R.string.september)
            9 -> monthOfYear = mContext.resources.getString(R.string.october)
            10 -> monthOfYear = mContext.resources.getString(R.string.november)
            11 -> monthOfYear = mContext.resources.getString(R.string.december)
            else -> {}
        }
        return monthOfYear
    }
    fun showDialogAlertReview(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_alert_review_layout)
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
            val mIntent = Intent(
                this@ParentsMeetingTimeSlotActivity,
                ReviewAppointmentsActivity::class.java
            )
            startActivity(mIntent)
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun showDialogAlertDoubleBtn(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialog_double_button_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
//            slotId = mListViewArrayPost!![0].getId().toString()
            postSelectedSlot()
            dialog.dismiss()
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun confirmApiCall(book_id:Int) {

        var bookID=book_id.toString()
        var slot:String="["+book_id+"]"
        var model= ConfirmApi(slot)
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

                            getPtaAllottedDateList()

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
                            getPtaAllottedDateList()
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

    private fun postSelectedSlot() {
        if(mListViewArrayPost!!.size>0){
            slotId = mListViewArrayPost!![0].id.toString()!!

        }

        val token = PreferenceManager.getUserCode(mContext)
        if (PreferenceManager.getPTABooked(mContext)){
        slotId = PreferenceManager.getPTABookedID(mContext)
            PreferenceManager.setPTABooked(mContext,false)
        }
        val body = PostSlotRequestModel(mStudentId,slotId)
        val call: Call<PostSlotResponseModel> =
            ApiClient.getClient.postSlot( body,"Bearer $token")
        call.enqueue(object : Callback<PostSlotResponseModel> {
            override fun onResponse(
                call: Call<PostSlotResponseModel>,
                response: Response<PostSlotResponseModel>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status==100){
                            var bookId:Int=response.body()!!.data.booking_id
                            confirmApiCall(bookId)
                            //  getPtaAllottedDateList()

                        }else if (response.body()!!.status!!.equals(109)){

                            showDialogAlertSingleBtn(
                                mContext as Activity,
                                "Alert",
                                "Request cancelled successfully",
                                R.drawable.tick,
                                R.drawable.round
                            )
                            getPtaAllottedDateList()

                            // var bookId:Int=response.body()!!.data.booking_id
                            // confirmApiCall(bookId)
                        }else{


                            Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
                        }

                    }else{

                        CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                    }
                }else{

                    CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<PostSlotResponseModel>, t: Throwable) {
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })

    }

    fun showDialogAlertSingleBtn(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
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
        dialogButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }



}