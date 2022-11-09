package com.nas.naisak.fragment.communications

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.communication.SocialMediaActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.commonmodels.SendStaffMailApiModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.WebviewLoader
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import com.nas.naisak.fragment.communications.adapter.CommunicationAdapter
import com.nas.naisak.fragment.communications.model.CommunicationListModel
import com.nas.naisak.fragment.communications.model.CommunicationListUseModel
import com.nas.naisak.fragment.communications.model.CommunicationResponseModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunicationFragment  : Fragment() {
    lateinit var mContext: Context
    lateinit var mListView: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var title: RelativeLayout
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var parentsEssentialArrayList: ArrayList<CommunicationListModel>
    lateinit var parentsEssentialArrayListUse: ArrayList<CommunicationListUseModel>
    lateinit var titleTextView: TextView
    lateinit var bannerImagePager: ImageView
    lateinit var sendEmail: ImageView
    lateinit var descriptionTV: TextView
    lateinit var ContactEmail: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parents_essentials, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            callParentsEssentialBanner()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initialiseUI() {
        mContext = requireContext()

        progressDialog = requireView().findViewById(R.id.progressDialog)
        titleTextView = view?.findViewById(R.id.titleTextView) as TextView
        bannerImagePager = requireView().findViewById(R.id.bannerImagePager)
        sendEmail = requireView().findViewById(R.id.sendEmail)
        descriptionTV = requireView().findViewById(R.id.descriptionTV)
        titleTextView.text = "Communications"
        mListView = requireView().findViewById(R.id.mListView)
        title = requireView().findViewById(R.id.title)
        linearLayoutManager = LinearLayoutManager(mContext)
        mListView.layoutManager = linearLayoutManager
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        sendEmail.setOnClickListener(View.OnClickListener {

            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
            var cancelButton = dialog.findViewById(R.id.cancelButton) as Button
            var submitButton = dialog.findViewById(R.id.submitButton) as Button
            var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
            var text_content = dialog.findViewById(R.id.text_content) as EditText
            iconImageView.setImageResource(R.drawable.roundemail)
            text_dialog.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    text_dialog.hint = ""
                    text_dialog.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                    text_dialog.setPadding(5, 5, 0, 0)
                } else {
                    text_dialog.hint = "Enter your subject here..."
                    text_dialog.gravity = Gravity.CENTER
                }
            }
            text_content.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    text_content.gravity = Gravity.LEFT
                } else {
                    text_content.gravity = Gravity.CENTER
                }
            }

            cancelButton.setOnClickListener()
            {
                dialog.dismiss()
            }
            submitButton.setOnClickListener()
            {
                if (text_dialog.text.toString().trim().equals("")) {
                    CommonMethods.showDialogueWithOk(
                        mContext,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
                        progressDialog.startAnimation(aniRotate)
                        var internetCheck = CommonMethods.isInternetAvailable(mContext)
                        if (internetCheck) {
                            callSendEmailToStaffApi(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                ContactEmail,
                                dialog,
                                progressDialog
                            )

                        } else {
                            CommonMethods.showSuccessInternetAlert(mContext)
                        }
                    }
                }
            }
            dialog.show()
        })
        mListView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if(position==0)
                {
                    val intent = Intent(mContext, SocialMediaActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(mContext, WebviewLoader::class.java)
                    intent.putExtra("webview_url", parentsEssentialArrayListUse[position].url)
                    intent.putExtra("title", parentsEssentialArrayListUse[position].title)
                    startActivity(intent)
                }


            }

        })
    }

    private fun callParentsEssentialBanner() {
        progressDialog.visibility = View.VISIBLE
        parentsEssentialArrayList= ArrayList()
        parentsEssentialArrayListUse= ArrayList()
        val call: Call<CommunicationResponseModel> = ApiClient.getClient.communication("Bearer "+ PreferenceManager.getUserCode(mContext))
        call.enqueue(object : Callback<CommunicationResponseModel> {
            override fun onFailure(call: Call<CommunicationResponseModel>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<CommunicationResponseModel>,
                response: Response<CommunicationResponseModel>
            ) {
                progressDialog.visibility = View.GONE

                if (response.body()!!.status == 100) {
                    var bannerImage=response.body()!!.data.banner_image
                    var contactEmail=response.body()!!.data.contact_email
                    ContactEmail = response.body()!!.data.contact_email
                    var description=response.body()!!.data.description
                    parentsEssentialArrayList.addAll(response.body()!!.data.parents_essentials)
                    if (bannerImage.isNotEmpty()){
                        context?.let {
                            Glide.with(it)
                                .load(bannerImage)
                                .into(bannerImagePager)
                        }!!
                    }else{
                        bannerImagePager.setBackgroundResource(R.drawable.default_banner)

                    }
                    if (contactEmail.equals("")) {
                        sendEmail.visibility = View.GONE
                        title.visibility = View.INVISIBLE
                    }
                    else {
                        sendEmail.visibility = View.VISIBLE
                        title.visibility = View.VISIBLE
                    }
                    if (description.equals(""))
                    {
                        descriptionTV.visibility=View.GONE
                    }
                    else {
                        descriptionTV.visibility = View.VISIBLE
                        descriptionTV.text = description
                    }

                    if (parentsEssentialArrayList.size>0)
                    {
                        for (i in 0..parentsEssentialArrayList.size)
                        {
                            var model=CommunicationListUseModel()
                            if (i==0)
                            {
                               model.id=1001
                               model.title="Social Media"
                               model.url=""
                            }
                            else{
                                model.id=parentsEssentialArrayList.get(i-1).id
                                model.title=parentsEssentialArrayList.get(i-1).title
                                model.url=parentsEssentialArrayList.get(i-1).url
                            }

                            parentsEssentialArrayListUse.add(model)

                        }
                    } else {
                        var model = CommunicationListUseModel()
                        model.id = 1001
                        model.title = "Social Media"
                        model.url = ""
                        parentsEssentialArrayListUse.add(model)
                    }

                    val parentsEssentialAdapter =
                        CommunicationAdapter(mContext, parentsEssentialArrayListUse)
                    mListView.adapter = parentsEssentialAdapter
                } else if (response.body()!!.status == 116) {


                    PreferenceManager.setUserCode(mContext, "")
                    PreferenceManager.setUserEmail(mContext, "")
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                } else {
                    if (response.body()!!.status == 101) {
                        CommonMethods.showDialogueWithOk(mContext, "Some error occured", "Alert")
                    }
                }

            }

        })
    }

    fun callSendEmailToStaffApi(
        title: String,
        message: String,
        staffEmail: String,
        dialog: Dialog,
        progressDialog: RelativeLayout
    ) {
        val token = PreferenceManager.getUserCode(mContext)
        val sendMailBody = SendStaffMailApiModel(staffEmail, title, message)
        val call: Call<ResponseBody> =
            ApiClient.getClient.sendStaffMail(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if (jsonObject.has("status")) {
                            val status: Int = jsonObject.optInt("status")
                            Log.e("STATUS LOGIN", status.toString())
                            if (status == 100) {
                                dialog.dismiss()
                                CommonMethods.showDialogueWithOk(
                                    mContext,
                                    "Successfully send the email.",
                                    "Success"
                                )
                                //dialog.dismiss()

                            } else if (status == 116) {
                                PreferenceManager.setUserCode(mContext, "")
                                PreferenceManager.setUserEmail(mContext, "")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                mContext.startActivity(mIntent)
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (response.code() == 116) {
                            PreferenceManager.setUserCode(mContext, "")
                            PreferenceManager.setUserEmail(mContext, "")
                            val mIntent = Intent(activity, LoginActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mContext.startActivity(mIntent)
                        }
                    }
                }
            }

        })
    }
}