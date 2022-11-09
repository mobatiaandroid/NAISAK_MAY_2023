package com.nas.naisak.activity.parents_meeting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.calender.ExtendedCalendarView
import com.nas.naisak.calender.model.CalendarEventsModel
import com.nas.naisak.commonmodels.AllottedDatesRequestModel
import com.nas.naisak.commonmodels.AllottedDatesResponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ParentsMeetingCalendarActivity: AppCompatActivity() {
    var calendarView: ExtendedCalendarView? = null
    var mContext: Context = this
    var continueImageView: ImageView? = null
    var home: ImageView? = null
    var mListViewArray: ArrayList<CalendarEventsModel>? = null
    var calendarEventsModelFilteredList: ArrayList<CalendarEventsModel>? = null
    var extras: Bundle? = null
    var cal: Calendar? = null
    var appointmentDateTV: TextView? = null
    private var mStaffId = ""
    private var selectedDate = ""
    private var mStudentId = ""
    private var mStudentName = ""
    private var mStaffName = ""
    private var mClass = ""

    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_meeting_calendar)
        mContext = this
        initialiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            getPtaAllotedDateList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)

        }
    }

    private fun getPtaAllotedDateList() {
        mListViewArray = ArrayList()
        val body = AllottedDatesRequestModel(mStudentId,mStaffId)
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<AllottedDatesResponseModel> =
            ApiClient.getClient.getPtaAllottedDates(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<AllottedDatesResponseModel> {
            override fun onResponse(
                call: Call<AllottedDatesResponseModel>,
                response: Response<AllottedDatesResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){

                            if (response.body()!!.data!!.lists!!.size > 0 ){
                                calendarView!!.setMonthPrevNextVisibility(2)

                                for (i in response.body()!!.data!!.lists!!.indices) {
                                    val dataDate: String = response.body()!!.data!!.lists!![i]!!
                                    mListViewArray!!.add(getSearchValues(dataDate))
                                }
                                calendarView!!.datesForDays(mListViewArray)
                            }else{

                                //CustomStatusDialog();
                                calendarView!!.setMonthPrevNextVisibility(1)
                                Toast.makeText(mContext, "No Dates Available", Toast.LENGTH_SHORT)
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

            override fun onFailure(call: Call<AllottedDatesResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE

                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })

    }

    private fun getSearchValues(eventdate: String): CalendarEventsModel {
        val mCalendarEventsModel = CalendarEventsModel()
        mCalendarEventsModel.seteventdate(eventdate)
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
        return mCalendarEventsModel
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

    private fun initialiseUI() {
        cal = Calendar.getInstance()
        extras = intent.extras
        if (extras != null) {
            mStaffId = extras!!.getString("staff_id")!!
            mStudentId = extras!!.getString("student_id")!!
            mStudentName = extras!!.getString("studentName")!!
            mStaffName = extras!!.getString("staffName")!!
            mClass = extras!!.getString("studentClass")!!
        }

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


        appointmentDateTV = findViewById<View>(R.id.appointmentDateTV) as TextView
        continueImageView = findViewById<View>(R.id.continueImageView) as ImageView
        calendarView = findViewById<View>(R.id.calendarView) as ExtendedCalendarView
//        mTitleTextView.setText(ClassNameConstants.PARENTS_MEETING)
//        headermanager = HeaderManager(this@ParentsEveningCalendarActivity, PARENT_EVENING)
//        headermanager.getHeader(relativeHeader, 0)
//        back = headermanager.getLeftButton()
//        headermanager.setButtonLeftSelector(
//            R.drawable.back,
//            R.drawable.back
//        )
//        back!!.setOnClickListener {
//            CommonMethods.hideKeyBoard(mContext)
//            finish()
//        }
//        home = headermanager.getLogoButton()
//        home!!.setOnClickListener {
//            val `in` = Intent(mContext, HomeActivity::class.java)
//            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(`in`)
//        }
        continueImageView!!.setOnClickListener {
            val mIntent = Intent(mContext, ParentsMeetingTimeSlotActivity::class.java)
            mIntent.putExtra("staff_id", mStaffId)
            mIntent.putExtra("student_id", mStudentId)
            mIntent.putExtra("studentName", mStudentName)
            mIntent.putExtra("staffName", mStaffName)
            mIntent.putExtra("studentClass", mClass)
            mIntent.putExtra("selectedDate", selectedDate)
            startActivity(mIntent)
            if (!PreferenceManager.getIBackParent(mContext)) {
                finish()
            }
        }
        calendarView!!.setOnDayClickListener { adapter, view, position, id, day -> // TODO Auto-generated method stub
            if (mListViewArray!!.size > 0) {
                calendarEventsModelFilteredList = ArrayList()
                for (i in mListViewArray!!.indices) {
                    if (day.getYear() === mListViewArray!!
                            .get(i).year && day.getMonth() === mListViewArray!!
                            .get(i).month && day.getDay() === mListViewArray!!
                            .get(i).day
                    ) {
                        calendarEventsModelFilteredList!!
                            .add(mListViewArray!![i])
                        break
                    }
                }
                if (calendarEventsModelFilteredList!!.size > 0) {
                    continueImageView!!.visibility = View.INVISIBLE
                    selectedDate = calendarEventsModelFilteredList!![0].geteventdate()
                    //                        appointmentDateTV.setText("You've selected appointment date\n" + calendarEventsModelFilteredList.get(0).getDay() + " " + calendarEventsModelFilteredList.get(0).getMonthString() + " " + calendarEventsModelFilteredList.get(0).getYear());
                    val mIntent = Intent(mContext, ParentsMeetingTimeSlotActivity::class.java)
                    mIntent.putExtra("staff_id", mStaffId)
                    mIntent.putExtra("student_id", mStudentId)
                    mIntent.putExtra("studentName", mStudentName)
                    mIntent.putExtra("staffName", mStaffName)
                    mIntent.putExtra("studentClass", mClass)
                    mIntent.putExtra("selectedDate", selectedDate)
                    PreferenceManager.setBackParent(mContext, true)
                    startActivity(mIntent)
                    /*  if(PreferenceManager.getIBackParent(mContext)){
                               finish();
                            }*/
                } else {
                    appointmentDateTV!!.text = ""
                    continueImageView!!.visibility = View.INVISIBLE
                }
            }
            /*	mMonthDay.setText(getMonth(day.getMonth()) + " "
                            + String.valueOf(day.getDay()));
                    mYear.setText(String.valueOf(day.getYear()));*/
        }
    }
}
