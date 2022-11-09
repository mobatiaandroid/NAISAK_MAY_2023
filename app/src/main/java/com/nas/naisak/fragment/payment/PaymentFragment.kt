package com.nas.naisak.fragment.payment

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
import com.bumptech.glide.Glide
import com.nas.naisak.R
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.payment.information.PaymentInformationActivity
import com.nas.naisak.activity.payment.payhere.PaymentActivity
import com.nas.naisak.commonmodels.SendStaffMailApiModel
import com.nas.naisak.constants.ApiClient
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.fragment.payment.model.PaymentBannerResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment : Fragment() {
    lateinit var mContext: Context
    lateinit var bannerImagePager: ImageView
    lateinit var sendEmail: ImageView
    lateinit var descriptionTV: TextView
    lateinit var progressDialog: RelativeLayout
    lateinit var paymentRelative: RelativeLayout
    lateinit var informationRelative: RelativeLayout
    lateinit var title: RelativeLayout
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var titleTextView: TextView
    lateinit var ContactEmail: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseUI()
        if (CommonMethods.isInternetAvailable(mContext)) {
            callPaymentBanner()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    @SuppressLint("SetTextI18n", "UseRequireInsteadOfGet")
    private fun initialiseUI() {
        mContext = requireContext()
        bannerImagePager = view!!.findViewById(R.id.bannerImagePager)
        sendEmail = view!!.findViewById(R.id.sendEmail)
        descriptionTV = view!!.findViewById(R.id.descriptionTV)
        progressDialog = view!!.findViewById(R.id.progressDialog)
        paymentRelative = view!!.findViewById(R.id.paymentRelative)
        informationRelative = view!!.findViewById(R.id.informationRelative)
        titleTextView = view?.findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Payments"
        title = view!!.findViewById(R.id.title)
        linearLayoutManager = LinearLayoutManager(mContext)
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        paymentRelative.setOnClickListener(View.OnClickListener {
            PreferenceManager.setStudentID(mContext, 0)
            val intent = Intent(activity, PaymentActivity::class.java)
            activity?.startActivity(intent)
//            CommonMethods.showDialogueWithOk(mContext, "Comming Soon", "Alert")
        })

        informationRelative.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, PaymentInformationActivity::class.java)
            activity?.startActivity(intent)
        })

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
    }


    private fun callPaymentBanner() {
        progressDialog.visibility = View.VISIBLE
        val call: Call<PaymentBannerResponse> =
            ApiClient.getClient.paymentBanner("Bearer " + PreferenceManager.getUserCode(mContext))
        call.enqueue(object : Callback<PaymentBannerResponse> {
            override fun onFailure(call: Call<PaymentBannerResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<PaymentBannerResponse>,
                response: Response<PaymentBannerResponse>
            ) {
                progressDialog.visibility = View.GONE

                if (response.body()!!.status == 100) {

                    val email: String = response.body()!!.data.contact_email
                    ContactEmail = response.body()!!.data.contact_email
                    val description: String = response.body()!!.data.description
                    val bannerImage: String = response.body()!!.data.banner_image

                    if (bannerImage.isNotEmpty()) {
                        context?.let {
                            Glide.with(it)
                                .load(bannerImage)
                                .into(bannerImagePager)
                        }!!
                    } else {
                        bannerImagePager.setBackgroundResource(R.drawable.default_banner)

                    }
                    if (email.equals("")) {
                        sendEmail.visibility = View.GONE
                        title.visibility = View.GONE
                    } else {
                        sendEmail.visibility = View.VISIBLE
                        title.visibility = View.VISIBLE
                    }
                    if (description.equals("")) {
                        descriptionTV.visibility = View.GONE
                    } else {
                        descriptionTV.visibility = View.VISIBLE
                        descriptionTV.text = description
                    }

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