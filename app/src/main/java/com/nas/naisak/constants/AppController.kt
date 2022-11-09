package com.nas.naisak.constants

import android.app.Application
import com.nas.naisak.activity.cca.model.CCADetailModel
import com.nas.naisak.activity.cca.model.WeekListModel

class AppController : Application() {
    companion object {
        @kotlin.jvm.JvmField
        var weekList: ArrayList<WeekListModel> = ArrayList()
        var instance: AppController? = null
        var mTitles: String? = null
        var eventIdList = ArrayList<String>()

        //        var weekList: ArrayList<WeekListModel>? = ArrayList()
        var weekListWithData: java.util.ArrayList<Int>? = ArrayList()
        var CCADetailModelArrayList: ArrayList<CCADetailModel> = ArrayList()
        var filledFlag = 0


    }

    //    override fun onCreate() {
//        super.onCreate()
//        mInstance=this
//        FirebaseApp.initializeApp(this)
//
//    }
    init {
        instance = this
    }

//    @Synchronized
//    fun getInstance(): AppController? {
//        return mInstance
//    }
}
