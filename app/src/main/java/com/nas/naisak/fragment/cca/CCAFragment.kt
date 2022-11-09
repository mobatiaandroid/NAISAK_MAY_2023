package com.nas.naisak.fragment.cca

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
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.cca.CCA_Activity
import com.nas.naisak.activity.cca.ExternalProviderActivity
import com.nas.naisak.activity.cca.InformationCCAActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.commonmodels.SendStaffMailApiModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.JsonConstants.Companion.EAP
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.fragment.cca.model.BannerResponseModel
import kotlinx.android.synthetic.main.fragment_c_c_a.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CCAFragment : Fragment() {
    var mTitleTextView: TextView? = null
    var descriptionTV: TextView? = null
    var ccaDot: TextView? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    var mtitleRel: LinearLayout? = null
    var externalCCA: RelativeLayout? = null
    var informationCCA: RelativeLayout? = null
    var bannerImagePager: ImageView? = null
    var mailImageView: ImageView? = null
    var ccaOption: RelativeLayout? = null
    var contactEmail = ""
    private var description = ""
    var text_content: TextView? = null
    var text_dialog: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(
            R.layout.fragment_c_c_a, container,
            false
        )

        mContext = activity
        initialiseUI()

        return mRootView
    }

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        descriptionTV = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        ccaDot = mRootView!!.findViewById<View>(R.id.ccaDot) as TextView
        mTitleTextView!!.text = EAP
        mtitleRel = mRootView!!.findViewById<View>(R.id.title) as LinearLayout

        externalCCA = mRootView!!.findViewById<View>(R.id.epRelative) as RelativeLayout
        ccaOption = mRootView!!.findViewById<View>(R.id.CcaOptionRelative) as RelativeLayout
        informationCCA = mRootView!!.findViewById<View>(R.id.informationRelative) as RelativeLayout
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        mailImageView = mRootView!!.findViewById<View>(R.id.mailImageView) as ImageView

        if (CommonMethods.isInternetAvailable(mContext!!)) {
            getList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext!!)
        }
        externalCCA!!.setOnClickListener {
            val intent = Intent(mContext, ExternalProviderActivity::class.java)
            intent.putExtra("tab_type", "External Providers")
            startActivity(intent)
        }
        informationCCA!!.setOnClickListener {
            val intent = Intent(mContext, InformationCCAActivity::class.java)
            intent.putExtra("tab_type", "Information")
            startActivity(intent)
        }
        ccaOption!!.setOnClickListener {
            if (!PreferenceManager.getUserCode(mContext!!).equals("")) {
                PreferenceManager.setStudIdForCCA(mContext!!, "")
                val intent = Intent(mContext, CCA_Activity::class.java)
                intent.putExtra("tab_type", "ECA Options")
                startActivity(intent)
            } else {
               CommonMethods.showDialogueWithOk(mContext!!,"This feature is available for Registered users only","Alert")
            }
        }
        mailImageView!!.setOnClickListener {
            val dialog = Dialog(mContext!!)
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
                        mContext!!,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        CommonMethods.showDialogueWithOk(
                            mContext!!,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
                        progressDialog.startAnimation(aniRotate)
                        var internetCheck = CommonMethods.isInternetAvailable(mContext!!)
                        if (internetCheck) {
                            callSendEmailToStaffApi(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),contactEmail, dialog, progressDialog)

                        } else {
                            CommonMethods.showSuccessInternetAlert(mContext!!)
                        }
                    }
                }
            }
            dialog.show()
        }

    }

    fun callSendEmailToStaffApi(
        title: String, message: String, staffEmail: String, dialog: Dialog, progressDialog: ProgressBar
    )
    {
        val token = PreferenceManager.getUserCode(mContext!!)
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
                                    mContext!!,
                                    "Successfully send the email.",
                                    "Success")
                                //dialog.dismiss()

                            }
                            else if(status==116)
                            {
                                PreferenceManager.setUserCode(mContext!!,"")
                                PreferenceManager.setUserEmail(mContext!!,"")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                mContext!!.startActivity(mIntent)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if(response.code()==116)
                        {
                            PreferenceManager.setUserCode(mContext!!,"")
                            PreferenceManager.setUserEmail(mContext!!,"")
                            val mIntent = Intent(activity, LoginActivity::class.java)
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            mContext!!.startActivity(mIntent)
                        }
                    }
                }
            }

        })
    }

    private fun getList() {
        val token = PreferenceManager.getUserCode(mContext!!)
        val call: Call<BannerResponseModel> =
            ApiClient.getClient.getBanner( "Bearer $token")
        call.enqueue(object : Callback<BannerResponseModel> {
            override fun onResponse(
                call: Call<BannerResponseModel>,
                response: Response<BannerResponseModel>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        if (response.body()!!.status.toString() == "100"){
                            val bannerImage: String = response.body()!!.data!!.banner_image!!
                            description = response.body()!!.data!!.description!!
                            contactEmail = response.body()!!.data!!.contact_email!!
//                            PreferenceManager.setCcaOptionBadge(
//                                mContext!!,
//                                response.body()!!.data!!.cca_badge!!
//                            )
//                            PreferenceManager.setCcaOptionEditedBadge(
//                                mContext!!,
//                                response.body()!!.data!!.cca_edited_badge!!
//                            )
//                            if (PreferenceManager.getCcaOptionBadge(mContext!!)
//                                    .equals("0") && PreferenceManager.getCcaOptionEditedBadge(
//                                    mContext!!
//                                ).equals("0")
//                            ) {
//                                ccaDot!!.setVisibility(
//                                    View.GONE
//                                )
//                            } else if (PreferenceManager.getCcaOptionBadge(mContext!!)
//                                    .equals("0") && !PreferenceManager.getCcaOptionEditedBadge(
//                                    mContext!!
//                                ).equals("0")
//                            ) {
//                                ccaDot!!.setVisibility(
//                                    View.VISIBLE
//                                )
//                                ccaDot!!.text =
//                                    response.body()!!.data!!.cca_badge.toString()
//                                ccaDot!!.setBackgroundResource(
//                                    R.drawable.shape_circle_navy
//                                )
//                            } else if (!PreferenceManager.getCcaOptionBadge(mContext!!)
//                                    .equals("0") && PreferenceManager.getCcaOptionEditedBadge(
//                                    mContext!!
//                                ).equals("0")
//                            ) {
//                                ccaDot!!.setVisibility(
//                                    View.VISIBLE
//                                )
//                                ccaDot!!.text =
//                                    response.body()!!.data!!.cca_badge.toString()
//
//                                ccaDot!!.setBackgroundResource(
//                                    R.drawable.shape_circle_red
//                                )
//                            } else if (!PreferenceManager.getCcaOptionBadge(mContext!!)
//                                    .equals("0") && !PreferenceManager.getCcaOptionEditedBadge(
//                                    mContext!!
//                                ).equals("0")
//                            ) {
//                                ccaDot!!.setVisibility(
//                                    View.VISIBLE
//                                )
//                                ccaDot!!.text =(
//                                    response.body()!!.data!!.cca_badge.toString()!!
//                                )
//                                ccaDot!!.setBackgroundResource(
//                                    R.drawable.shape_circle_red
//                                )
//                            }
                            if (!bannerImage.equals("", ignoreCase = true)) {
                                Glide.with(mContext!!).load(CommonMethods.replace(bannerImage)).fitCenter()

                                    .centerCrop().into(bannerImagePager!!)

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
                            } else {
                                bannerImagePager!!.setBackgroundResource(R.drawable.default_banner)
//											bannerImagePager.setBackgroundResource(R.drawable.ccas_banner);
                            }
                            println("contact mail$contactEmail")
                            if (description.equals("", ignoreCase = true) && contactEmail.equals(
                                    "",
                                    ignoreCase = true
                                )
                            ) {
                                mtitleRel!!.visibility = View.GONE
                            } else {
                                mtitleRel!!.visibility = View.VISIBLE
                            }
                            if (description.equals("", ignoreCase = true)) {
                                descriptionTV!!.visibility = View.GONE
                                //  descriptionTitle.setVisibility(View.GONE);
                            } else {
                                descriptionTV!!.text = description
                                descriptionTV!!.visibility = View.VISIBLE
                                mtitleRel!!.visibility = View.VISIBLE
                                // mtitleRel.setVisibility(View.VISIBLE);
                            }
                            if (contactEmail.equals("", ignoreCase = true)) {
                                println("contact mail1")
                                mailImageView!!.visibility = View.GONE
                            } else {
                                println("contact mail2")
                                mtitleRel!!.visibility = View.VISIBLE
                                mailImageView!!.visibility = View.VISIBLE
                            }
                            // CCAFRegisterRel.setVisibility(View.VISIBLE);
                            // CCAFRegisterRel.setVisibility(View.VISIBLE);

                        }else{
                            CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                        }
                    }else{
                        CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                    }
                }else{
                    CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
                }
            }

            override fun onFailure(call: Call<BannerResponseModel>, t: Throwable) {
                CommonMethods.showDialogueWithOk(mContext!!,getString(R.string.common_error),"Alert")
            }

        })
    }

}