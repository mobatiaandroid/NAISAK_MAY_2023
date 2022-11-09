package com.nas.naisak.constants

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.nas.naisak.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommonMethods {
    companion object{

        fun isInternetAvailable(context: Context): Boolean
        {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }


        fun showSuccessInternetAlert(context: Context)
        {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogue_no_internet)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
            var alertHead = dialog.findViewById(R.id.alertHead) as TextView
            var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
            var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
            text_dialog.text = "Network error occurred. Please check your internet connection and try again later"
            alertHead.text = "Alert"
            iconImageView.setBackgroundResource(R.drawable.roundred)
            iconImageView.setImageResource(R.drawable.nonetworkicon)
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()

            }
            dialog.show()
        }

        fun showDialogueWithOk(context: Context, message: String, msgHead: String)
        {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogue_ok_alert)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as? ImageView
            var alertHead = dialog.findViewById(R.id.alertHead) as? TextView
            var text_dialog = dialog.findViewById(R.id.text_dialog) as? TextView
            var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
            text_dialog?.text = message
            alertHead?.text = msgHead
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()
            }
            dialog.show()
        }
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun htmlparsing(text: String): String? {
            var encodedString: String
            encodedString = text.replace("&lt;".toRegex(), "<")
            encodedString = encodedString.replace("&gt;".toRegex(), ">")
            encodedString = encodedString.replace("&amp;".toRegex(), "")
            encodedString = encodedString.replace("amp;".toRegex(), "")
            return encodedString
        }


        fun showDialogueWithOkPay(context: Context, message: String, msgHead: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogue_ok_alert)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as? ImageView
            var alertHead = dialog.findViewById(R.id.alertHead) as? TextView
            var text_dialog = dialog.findViewById(R.id.text_dialog) as? TextView
            var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
            text_dialog?.text = message
            alertHead?.text = msgHead
            iconImageView?.setImageResource(R.drawable.exclamationicon)
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()
            }
            dialog.show()
        }


        fun Toast.showCustomToastSuccess(message: String, activity: Activity) {
            val layout = activity.layoutInflater.inflate(
                R.layout.custom_toast_success,
                activity.findViewById(R.id.toast_container)
            )

            // set the text of the TextView of the message
            val textView = layout.findViewById<TextView>(R.id.toast_text)
            textView.text = message

            // use the application extension function
            this.apply {
                setGravity(Gravity.TOP, 10, 10)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }


        fun Toast.showCustomToastError(message: String, activity: Activity) {
            val layout = activity.layoutInflater.inflate(
                R.layout.custom_toast_error,
                activity.findViewById(R.id.toast_container)
            )

            // set the text of the TextView of the message
            val textView = layout.findViewById<TextView>(R.id.toast_text)
            textView.text = message

            // use the application extension function
            this.apply {
                setGravity(Gravity.TOP, 10, 10)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }

        fun replace(str: String): String? {
            return str.replace(" ".toRegex(), "%20")
        }

        fun hideKeyBoard(context: Context) {
            val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.windowToken, 0
                )
            }
        }

        fun replaceam(str: String): String? {
            return str.replace("am".toRegex(), " ")
        }

        fun replacepm(str: String): String? {
            return str.replace("pm".toRegex(), " ")
        }

        fun replaceAM(str: String): String? {
            return str.replace("AM".toRegex(), " ")
        }

        fun replacePM(str: String): String? {
            return str.replace("PM".toRegex(), " ")
        }

        fun replaceamdot(str: String): String? {
            return str.replace("a.m.".toRegex(), " ")
        }

        fun replacepmdot(str: String): String? {
            return str.replace("p.m.".toRegex(), " ")
        }

        fun replaceAMDot(str: String): String? {
            return str.replace("A.M.".toRegex(), " ")
        }

        fun replacePMDot(str: String): String? {
            return str.replace("P.M.".toRegex(), " ")
        }

        fun replaceAmdot(str: String): String? {
            return str.replace("a.m.".toRegex(), "am")
        }

        fun replacePmdot(str: String): String? {
            return str.replace("p.m.".toRegex(), "pm")
        }


        fun dateParsingTodd_MMM_yyyy(date: String): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
    }
}