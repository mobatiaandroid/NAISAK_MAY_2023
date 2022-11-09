package com.nas.naisak.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.nas.naisak.R
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.activity.login.LoginActivity
import com.nas.naisak.activity.tutorial.TutorialActivity
import com.nas.naisak.constants.PreferenceManager

class SplashActivity : AppCompatActivity() {

    lateinit var mContext: Context
    val SPLASH_TIME_OUT:Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext=this
        Handler(Looper.myLooper()!!).postDelayed({

         if(!PreferenceManager.getIsFirstLaunch(mContext))
         {
             PreferenceManager.isFirstLaunch(mContext,true)
             val intent = Intent(mContext, TutorialActivity::class.java)
             intent.putExtra("isFrom", "login")
             startActivity(intent)
//             startActivity(Intent(this, TutorialActivity::class.java))
             finish()
         }
          else
         {
             if (PreferenceManager.getUserCode(mContext).equals(""))
             {

                 startActivity(Intent(this, LoginActivity::class.java))
                 finish()
             }
             else
             {
                 startActivity(Intent(this, HomeActivity::class.java))
                 finish()
             }
         }
        }, SPLASH_TIME_OUT)
    }
}