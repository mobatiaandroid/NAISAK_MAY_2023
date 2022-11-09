package com.nas.naisak.activity.cca

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.InformationRecyclerAdapter
import com.nas.naisak.activity.cca.model.CCAInfoRequestModel
import com.nas.naisak.activity.cca.model.CCAInfoResponseModel
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.*
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationCCAActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progressBar: ProgressBar
    var extras: Bundle? = null
    var tab_type: String? = null
    var relativeHeader: RelativeLayout? = null
//    var mStudentSpinner: LinearLayout? = null
//    var studImg: ImageView? = null
//    var studName: TextView? = null
    var mnewsLetterListView: RecyclerView? = null
//    var textViewYear: TextView? = null
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var section = ""
    private val mListViewArray: ArrayList<CCAInfoResponseModel.Data.Lists> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_ccaactivity)
        mContext = this
        initilaiseUI()
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        if (CommonMethods.isInternetAvailable(mContext)) {
            getList()
        } else {
            CommonMethods.showSuccessInternetAlert(mContext)
        }
    }

    private fun getList() {

        val body = CCAInfoRequestModel("1")
        val token = PreferenceManager.getUserCode(mContext)
        val call: Call<CCAInfoResponseModel> =
            ApiClient.getClient.getCCAInfo(body, "Bearer $token")
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<CCAInfoResponseModel> {
            override fun onResponse(
                call: Call<CCAInfoResponseModel>,
                response: Response<CCAInfoResponseModel>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()!!.status.toString() == "100") {

                            if (response.body()!!.data!!.lists!!.isNotEmpty()) {
                                for (i in response.body()!!.data!!.lists!!.indices) {
                                    Log.e("respoje", response.body()!!.data!!.lists!![i]!!.url.toString())
                                    mListViewArray.add(response.body()!!.data!!.lists!![i]!!)
                                }
                                mnewsLetterListView!!.adapter =
                                    InformationRecyclerAdapter(mContext, mListViewArray)

                            } else {
                                CommonMethods.showDialogueWithOk(
                                    mContext,
                                    getString(R.string.common_error),
                                    "Alert"
                                )
                            }
                        } else {
                            CommonMethods.showDialogueWithOk(
                                mContext,
                                getString(R.string.common_error),
                                "Alert"
                            )
                        }
                    } else {
                        CommonMethods.showDialogueWithOk(
                            mContext,
                            getString(R.string.common_error),
                            "Alert"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CCAInfoResponseModel>, t: Throwable) {
                progressBar.visibility = View.GONE

                CommonMethods.showDialogueWithOk(
                    mContext,
                    getString(R.string.common_error),
                    "Alert"
                )
            }


        })
    }

    private fun initilaiseUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
        progressBar = findViewById(R.id.progress)
        logoclick = findViewById(R.id.logoClickImgView)
        backRelative = findViewById(R.id.backRelative)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
//        mStudentSpinner = findViewById<View>(R.id.studentSpinner) as LinearLayout
//        studImg = findViewById<View>(R.id.imagicon) as ImageView
//        studName = findViewById<View>(R.id.studentName) as TextView
//        textViewYear = findViewById<View>(R.id.textViewYear) as TextView
        mnewsLetterListView = findViewById<View>(R.id.mnewsLetterListView) as RecyclerView
        mnewsLetterListView!!.setHasFixedSize(true)
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider_teal)!!)
        mnewsLetterListView!!.addItemDecoration(divider)
//        mnewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider_teal)))
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mnewsLetterListView!!.layoutManager = llm
        mnewsLetterListView!!.addOnItemClickListener(object : OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (mListViewArray[position].url!!.endsWith(".pdf")) {
                    val intent = Intent(mContext, PdfReaderActivity::class.java)
                    intent.putExtra("pdf_url", mListViewArray[position].url)
                    intent.putExtra("pdf_title", mListViewArray[position].title)
                    startActivity(intent)
                } else {
                    val intent = Intent(mContext, WebviewLoader::class.java)
                    intent.putExtra("webview_url", mListViewArray[position].url)
                    intent.putExtra("title", mListViewArray[position].title)
                    mContext.startActivity(intent)
                }
            }

        })

    }
}