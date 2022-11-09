package com.nas.naisak.fragment.parents_meeting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.parents_meeting.ParentsMeetingCalendarActivity
import com.nas.naisak.activity.parents_meeting.ParentsMeetingInfoActivity
import com.nas.naisak.activity.parents_meeting.ReviewAppointmentsActivity
import com.nas.naisak.commonmodels.GetStaffByStudentApiModel
import com.nas.naisak.commonmodels.StaffListByStudentResponseModel
import com.nas.naisak.commonmodels.StudentListReponseModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.ClassNameConstants
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.parents_meeting.adapter.ParentsMeetingStaffAdapter
import com.nas.naisak.fragment.parents_meeting.adapter.ParentsMeetingStudentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [ParentsMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentsMeetingFragment : Fragment() {
    lateinit var mContext : Context
    var mStaffId = ""
    var mStudentId = ""
    var mStudentName = ""
    var mStaffName = ""
    var mClass = ""
    private lateinit var staffRelative: RelativeLayout
    private lateinit var studentRelative:RelativeLayout
    private lateinit var relativeMain:RelativeLayout
    private lateinit var selectStaffImgView: ImageView
    private lateinit var selectStudentImgView: ImageView
    private lateinit var next: ImageView
    private lateinit var infoImg: ImageView
    private lateinit var reviewImageView: ImageView
    lateinit var mTitleTextView: TextView
    lateinit var studentNameTV:TextView
    lateinit var staffNameTV:TextView
    var mListViewArray: ArrayList<StudentListReponseModel.Data.Lists>? = null
    var mListViewStaffArray: ArrayList<StaffListByStudentResponseModel.Data.Lists>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContext = requireActivity()

        return inflater.inflate(R.layout.fragment_parents_meeting, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            getStudentList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun getStudentList() {
        mListViewArray = ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<StudentListReponseModel> =
            ApiClient.getClient.getStudent( "Bearer $token")
        call.enqueue(object : Callback<StudentListReponseModel> {
            override fun onResponse(
                call: Call<StudentListReponseModel>,
                response: Response<StudentListReponseModel>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.lists!!.size > 0 ){

                                for (i in response.body()!!.data!!.lists!!.indices){
                                    mListViewArray!!.add(response.body()!!.data!!.lists!![i]!!)
                                }
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
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })


    }

    private fun initialiseUI() {
        mTitleTextView = requireView().findViewById<View>(R.id.titleTextView) as TextView
        studentNameTV = requireView().findViewById<View>(R.id.studentNameTV) as TextView
        staffNameTV = requireView().findViewById<View>(R.id.staffNameTV) as TextView
        selectStaffImgView = requireView().findViewById<View>(R.id.selectStaffImgView) as ImageView
        next = requireView().findViewById<View>(R.id.next) as ImageView
        selectStudentImgView = requireView().findViewById<View>(R.id.selectStudentImgView) as ImageView
        studentRelative = requireView().findViewById<View>(R.id.studentRelative) as RelativeLayout
        staffRelative = requireView().findViewById<View>(R.id.staffRelative) as RelativeLayout
        mTitleTextView.setText(ClassNameConstants.PARENTS_MEETING)
        relativeMain = requireView().findViewById<View>(R.id.relMain) as RelativeLayout
        reviewImageView = requireView().findViewById<View>(R.id.reviewImageView) as ImageView
        infoImg = requireView().findViewById<View>(R.id.infoImg) as ImageView
        infoImg.setOnClickListener {
            val mIntent = Intent(mContext, ParentsMeetingInfoActivity::class.java)
            mContext.startActivity(mIntent)
        }
        relativeMain.setOnClickListener(View.OnClickListener { })
//        if (PreferenceManager.getIsFirstTimeInPE(mContext)) {
//            PreferenceManager.setIsFirstTimeInPE(mContext, false)
//            val mintent = Intent(mContext, ParentsMeetingInfoActivity::class.java)
//            mintent.putExtra("type", 1)
//            mContext.startActivity(mintent)
//        }
        selectStaffImgView.setOnClickListener {
            if (mListViewStaffArray!!.size > 0) {
                showStaffList()
            } else {
                Toast.makeText(mContext, "No Staff Found.", Toast.LENGTH_SHORT).show()
            }
        }
        selectStudentImgView.setOnClickListener {
            if (mListViewArray!!.size > 0) {
                showStudentList()
            } else {
                Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show()
            }
        }
        next.setOnClickListener {
            val mIntent = Intent(activity, ParentsMeetingCalendarActivity::class.java)
            mIntent.putExtra("staff_id", mStaffId)
            mIntent.putExtra("student_id", mStudentId)
            mIntent.putExtra("studentName", mStudentName)
            mIntent.putExtra("staffName", mStaffName)
            mIntent.putExtra("studentClass", mClass)
            mContext.startActivity(mIntent)
        }
        reviewImageView.setOnClickListener {
            val mIntent = Intent(activity, ReviewAppointmentsActivity::class.java)
            mContext.startActivity(mIntent)
        }
    }

    private fun showStudentList() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())

        //if(mSocialMediaArray.get())
        iconImageView.setImageResource(R.drawable.boy)


        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.boy))
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button))
        } else {
            iconImageView.background = mContext.resources.getDrawable(R.drawable.boy)
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button)
        }
        val divider = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        socialMediaList.addItemDecoration(divider)
        socialMediaList.setHasFixedSize(true)

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsMeetingStudentAdapter(mContext, mListViewArray)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                selectStudentImgView.setImageResource(R.drawable.student)
                studentNameTV.setText(mListViewArray!![position].name)
                mStudentId = mListViewArray!![position].id.toString()
                mStudentName = mListViewArray!![position].name.toString()
                mClass = mListViewArray!![position].student_class.toString()
                staffRelative.visibility = View.INVISIBLE
                selectStaffImgView.setImageResource(R.drawable.addiconinparentsevng)
                staffNameTV.text = "Staff Name:-"
                next.visibility = View.GONE
                if (!mListViewArray!![position].photo.equals("")) {
                    Glide.with(mContext).load(
                        CommonMethods.replace(
                            mListViewArray!![position].photo.toString()
                        )
                    ).placeholder(R.drawable.student).error(R.drawable.student)
                        .fitCenter().into(selectStudentImgView)
                } else {
                    selectStudentImgView.setImageResource(R.drawable.student)
                }
                if (CommonMethods.isInternetAvailable(mContext)) {
                    getStaffList(mListViewArray!![position].id)
                } else {
                    CommonMethods.showSuccessInternetAlert(mContext)
                }
                dialog.dismiss()
            }})
        dialog.show()
    }

    private fun getStaffList(id: Int?) {
        mListViewStaffArray = ArrayList()
        val token = PreferenceManager.getUserCode(mContext)
        val getStaffBody = GetStaffByStudentApiModel(id.toString())
        val call: Call<StaffListByStudentResponseModel> =
            ApiClient.getClient.getStaffByStudent(getStaffBody, "Bearer $token")
        call.enqueue(object : Callback<StaffListByStudentResponseModel> {
            override fun onResponse(
                call: Call<StaffListByStudentResponseModel>,
                response: Response<StaffListByStudentResponseModel>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            if (response.body()!!.data!!.lists!!.size > 0 ){
                                for (i in response.body()!!.data!!.lists!!.indices){
                                    mListViewStaffArray!!.add(response.body()!!.data!!.lists!![i]!!)
                                }
                                staffRelative.visibility = View.VISIBLE
                            }else{

                                //CustomStatusDialog();
                                staffRelative.visibility = View.INVISIBLE
                                CommonMethods.showDialogueWithOk(mContext,"No staff found","Alert")
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

            override fun onFailure(call: Call<StaffListByStudentResponseModel>, t: Throwable) {
                CommonMethods.showDialogueWithOk(mContext,getString(R.string.common_error),"Alert")
            }

        })
    }

    private fun showStaffList() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_staff_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())

        //if(mSocialMediaArray.get())
        iconImageView.setImageResource(R.drawable.girl)
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.girl))
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button))
        } else {
            iconImageView.background = mContext.resources.getDrawable(R.drawable.girl)
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button)
        }

        val divider = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        socialMediaList.addItemDecoration(divider)
        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsMeetingStaffAdapter(mContext, mListViewStaffArray)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                selectStaffImgView.setImageResource(R.drawable.girl)
                staffNameTV.text = mListViewStaffArray!![position].name
                mStaffId = mListViewStaffArray!![position].id.toString()
                mStaffName = mListViewStaffArray!![position].name.toString()
                if (!mListViewStaffArray!![position].image_url.equals("")) {
                    Glide.with(mContext).load(
                        CommonMethods.replace(
                            mListViewStaffArray!![position].image_url.toString()
                        )
                    ).fitCenter().placeholder(R.drawable.girl).error(R.drawable.girl)
                        .fitCenter().into(selectStaffImgView)
                } else {
                    selectStaffImgView.setImageResource(R.drawable.girl)
                }
                next.visibility = View.VISIBLE
                dialog.dismiss()
            }})

        dialog.show()
    }


}