package com.nas.naisak.fcm

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.nas.naisak.constants.PreferenceManager

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    var mContext: Context = this
    override fun onTokenRefresh() {

        //val refreshedToken = FirebaseInstanceId.getInstance().token

        val refreshedToken = FirebaseInstanceId.getInstance().token.toString()

        Log.e("FIREBASETOKEN", refreshedToken)
        sendRegistrationToServer(refreshedToken)
        super.onTokenRefresh()
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        if (refreshedToken != null) {
            PreferenceManager.setFcmID(mContext, refreshedToken)
        }

    }
}