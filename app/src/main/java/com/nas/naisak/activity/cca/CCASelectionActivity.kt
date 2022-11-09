package com.nas.naisak.activity.cca

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nas.naisak.R
import com.nas.naisak.activity.cca.adapter.CCAsActivityAdapter
import com.nas.naisak.activity.cca.adapter.CCAsWeekListAdapter
import com.nas.naisak.activity.cca.model.CCADetailModel
import com.nas.naisak.activity.cca.model.WeekListModel
import com.nas.naisak.activity.home.HomeActivity
import com.nas.naisak.constants.AppController
import com.nas.naisak.constants.CommonMethods
import com.nas.naisak.constants.PreferenceManager
import com.nas.naisak.constants.recyclermanager.ItemOffsetDecoration
import com.nas.naisak.constants.recyclermanager.OnItemClickListener
import com.nas.naisak.constants.recyclermanager.addOnItemClickListener

class CCASelectionActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var back: ImageView
    lateinit var backRelative: RelativeLayout
    lateinit var logoclick: ImageView
    lateinit var progress: ProgressBar

    var CCADetailModelArrayList: ArrayList<CCADetailModel>? = null

    //    ArrayList<String> weekList;
    var relativeHeader: RelativeLayout? = null
    lateinit var msgRelative: RelativeLayout

    var tab_type = "CCAs"
    var extras: Bundle? = null

    //    ArrayList<String> mCcaArrayList;
    var recycler_review: RecyclerView? = null
    var weekRecyclerList: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recyclerweekViewLayoutManager: GridLayoutManager? = null
    var pos = 0
    var ccaDetailpos = 0
    var submitBtn: Button? = null
    var nextBtn: Button? = null
    var filled = false
    var weekSelected = false
    var weekPosition = 0
    var flag = 0
    var ccaedit = 0
    var mCCAsWeekListAdapter: CCAsWeekListAdapter? = null
    var TVselectedForWeek: TextView? = null
    var textViewCCAaSelect: TextView? = null
    var textViewStudName: TextView? = null
    var messageTxt: TextView? = null
    var mCCAsActivityAdapter: CCAsActivityAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ccaselection)
        mContext = this
        initialiseUI()

    }

    private fun initialiseUI() {
        titleTextView = findViewById(R.id.heading)
        back = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoclick = findViewById(R.id.logoClickImgView)
        progress = findViewById(R.id.progress)
        extras = intent.extras
        logoclick.setOnClickListener {
            val mIntent = Intent(mContext, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(mIntent)
        }
        backRelative.setOnClickListener {
            finish()
        }
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
            ccaedit = extras!!.getInt("ccaedit", 0)
            //            pos = extras.getInt("pos");
            CCADetailModelArrayList =
                extras!!.getSerializable("CCA_Detail") as ArrayList<CCADetailModel>
        }


        AppController.weekList = ArrayList()
        AppController.weekListWithData = ArrayList()
//        weekList.add("Sunday");
//        weekList.add("Monday");
//        weekList.add("Tuesday");
//        weekList.add("Wednesday");
//        weekList.add("Thursday");
//        weekList.add("Friday");
//        weekList.add("Saturday");
        //        weekList.add("Sunday");
//        weekList.add("Monday");
//        weekList.add("Tuesday");
//        weekList.add("Wednesday");
//        weekList.add("Thursday");
//        weekList.add("Friday");
//        weekList.add("Saturday");
        for (i in 0..6) {
            val mWeekListModel = WeekListModel()
            mWeekListModel.setWeekDay(getWeekday(i))
            mWeekListModel.setWeekDayMMM(getWeekdayMMM(i))
            if (ccaedit == 0) {
                mWeekListModel.setChoiceStatus("0")
                mWeekListModel.setChoiceStatus1("0")
            } else {
                mWeekListModel.setChoiceStatus("1")
                mWeekListModel.setChoiceStatus1("1")
            }
            AppController.weekList!!.add(mWeekListModel)
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        msgRelative = findViewById<View>(R.id.msgRelative) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        weekRecyclerList = findViewById<View>(R.id.weekRecyclerList) as RecyclerView
        TVselectedForWeek = findViewById<View>(R.id.TVselectedForWeek) as TextView
        textViewCCAaSelect = findViewById<View>(R.id.textViewCCAaSelect) as TextView
        textViewStudName = findViewById<View>(R.id.textViewStudName) as TextView
        messageTxt = findViewById<View>(R.id.messageTxt) as TextView
        submitBtn = findViewById<View>(R.id.submitBtn) as Button
        nextBtn = findViewById<View>(R.id.nextBtn) as Button
        nextBtn!!.getBackground().setAlpha(255)
        submitBtn!!.getBackground().setAlpha(150)

        val startAnimation = AnimationUtils.loadAnimation(
            applicationContext, R.anim.blinking_animation
        )
        messageTxt!!.startAnimation(startAnimation)
        if (PreferenceManager.getStudClassForCCA(mContext).equals("")) {
            textViewStudName!!.setText(PreferenceManager.getStudNameForCCA(mContext))
        } else {
            textViewStudName!!.text = Html.fromHtml(
                PreferenceManager.getStudNameForCCA(mContext)
                    .toString() + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(
                    mContext
                )
            )
        }
        if (ccaedit == 0) {
            CommonMethods.showDialogueWithOk(mContext,"Please select a CCA or None for each choice and each day","Info")

            submitBtn!!.getBackground().setAlpha(150)
            submitBtn!!.setVisibility(View.INVISIBLE)
        } else {
            submitBtn!!.getBackground().setAlpha(255)
            submitBtn!!.setVisibility(View.VISIBLE)
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.GONE)
            AppController.filledFlag = 1
        }

        submitBtn!!.setOnClickListener(View.OnClickListener { //              for (int i=0;i<CCADetailModelArrayList.size();i++)
            //              {
            //                  System.out.println("Choice1 "+CCADetailModelArrayList.get(i).getDay()+":"+CCADetailModelArrayList.get(i).getChoice1());
            //                  System.out.println("Choice2 "+CCADetailModelArrayList.get(i).getDay()+":"+CCADetailModelArrayList.get(i).getChoice2());
            //              }
            Log.e("filled1",filled.toString())
//            if (flag == 1) {
//                filled = true
//            }
            Log.e("flag",AppController.filledFlag.toString())

            if(AppController.filledFlag == 1){
//            if (filled) {
                val mInent = Intent(this@CCASelectionActivity, CCAsReviewActivity::class.java)
                Log.e("size selection", CCADetailModelArrayList!!.size.toString())
                AppController.CCADetailModelArrayList.clear()
                for (i in CCADetailModelArrayList!!.indices){
                    AppController.CCADetailModelArrayList.add(CCADetailModelArrayList!![i])
                }
                intent.putExtra("detail_array", CCADetailModelArrayList)
                startActivity(mInent)
            } else {
                CommonMethods.showDialogueWithOk(mContext,"Select choice for all available days","Alert")

            }
        })

        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recycler_review!!.addItemDecoration(itemDecoration)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
//        for (int i = 0; i < CCADetailModelArrayList.size(); i++)
//            if (CCADetailModelArrayList.get(i).getDay().equalsIgnoreCase("Sunday")) {
//                {
//                    ccaDetailpos=i;
//                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(i).getCcaChoiceModel(), CCADetailModelArrayList.get(i).getCcaChoiceModel2(),0,AppController.weekList);
//                    recycler_review.setAdapter(mCCAsActivityAdapter);
//                    break;
//                }
//            }

        //        for (int i = 0; i < CCADetailModelArrayList.size(); i++)
//            if (CCADetailModelArrayList.get(i).getDay().equalsIgnoreCase("Sunday")) {
//                {
//                    ccaDetailpos=i;
//                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(i).getCcaChoiceModel(), CCADetailModelArrayList.get(i).getCcaChoiceModel2(),0,AppController.weekList);
//                    recycler_review.setAdapter(mCCAsActivityAdapter);
//                    break;
//                }
//            }
        TVselectedForWeek!!.text = "Sunday"
//        for (int j = 0; j < AppController.weekList.size(); j++) {
//            for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
//                if (!AppController.weekList.get(j).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
//                    AppController.weekList.get(j).setChoiceStatus("2");
//                    AppController.weekList.get(j).setChoiceStatus1("2");
//                }
//                else
//                {
//                    AppController.weekList.get(j).setChoiceStatus("0");
//                    AppController.weekList.get(j).setChoiceStatus1("0");
//                }
//            }
//        }

        //        for (int j = 0; j < AppController.weekList.size(); j++) {
//            for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
//                if (!AppController.weekList.get(j).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
//                    AppController.weekList.get(j).setChoiceStatus("2");
//                    AppController.weekList.get(j).setChoiceStatus1("2");
//                }
//                else
//                {
//                    AppController.weekList.get(j).setChoiceStatus("0");
//                    AppController.weekList.get(j).setChoiceStatus1("0");
//                }
//            }
//        }
        for (i in 0 until AppController.weekList!!.size) {
            AppController.weekList!!.get(i).setChoiceStatus("2")
            AppController.weekList!!.get(i).setChoiceStatus1("2")
            AppController.weekList!!.get(i).setDataInWeek("0")
        }


        for (i in 0 until AppController.weekList!!.size) {
            for (j in CCADetailModelArrayList!!.indices) {
                if (AppController.weekList!!.get(i).getWeekDay().equals(
                        CCADetailModelArrayList!!.get(j).getDay()
                    )
                ) {
                    if (ccaedit == 0) {
                        AppController.weekList!!.get(i).setChoiceStatus("0")
                        AppController.weekList!!.get(i).setChoiceStatus1("0")
                    } else {
                        AppController.weekList!!.get(i).setChoiceStatus("1")
                        AppController.weekList!!.get(i).setChoiceStatus1("1")
                    }
                    AppController.weekList!!.get(i).setDataInWeek("1")
                    AppController.weekListWithData!!.add(i)
                }
            }
        }
        for (i in this.CCADetailModelArrayList!!.indices) {
            if (CCADetailModelArrayList!!.get(i).getDay()
                    .equals("Sunday")
            ) {
                ccaDetailpos = i
                textViewCCAaSelect!!.visibility = View.VISIBLE
                TVselectedForWeek!!.visibility = View.VISIBLE
                mCCAsActivityAdapter = CCAsActivityAdapter(
                    mContext,
                    CCADetailModelArrayList!!.get(i).getCcaChoiceModel(),
                    CCADetailModelArrayList!!.get(i).getCcaChoiceModel2(),
                    0,
                    AppController.weekList,
                    weekRecyclerList,ccaedit,CCADetailModelArrayList,nextBtn, submitBtn, filled,ccaDetailpos,msgRelative
                )
                recycler_review!!.adapter = mCCAsActivityAdapter
                break
            } else if (i == CCADetailModelArrayList!!!!.size - 1) {
                if (!CCADetailModelArrayList!!.get(i).getDay()
                        .equals("Sunday",ignoreCase = true)
                ) {
                    mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0)
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    textViewCCAaSelect!!.visibility = View.GONE
                    TVselectedForWeek!!.visibility = View.GONE
                    AppController.weekList!!.get(0).setChoiceStatus("2")
                    AppController.weekList!!.get(0).setChoiceStatus1("2")
                    //                    Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();
                }
            }
        }


//        CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(0).getCcaChoiceModel(), CCADetailModelArrayList.get(0).getCcaChoiceModel2());
//        recycler_review.setAdapter(mCCAsActivityAdapter);


//        CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(0).getCcaChoiceModel(), CCADetailModelArrayList.get(0).getCcaChoiceModel2());
//        recycler_review.setAdapter(mCCAsActivityAdapter);
        weekRecyclerList!!.setHasFixedSize(true)
//        recyclerweekViewLayoutManager = new GridLayoutManager(mContext, 7);
        //        recyclerweekViewLayoutManager = new GridLayoutManager(mContext, 7);
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.HORIZONTAL
//        weekRecyclerList.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//        weekRecyclerList.addItemDecoration(itemDecoration);
        //        weekRecyclerList.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//        weekRecyclerList.addItemDecoration(itemDecoration);
        weekRecyclerList!!.layoutManager = llm
//        weekRecyclerList.setLayoutManager(recyclerweekViewLayoutManager);
        //        weekRecyclerList.setLayoutManager(recyclerweekViewLayoutManager);
        mCCAsWeekListAdapter = CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition, msgRelative)
        weekRecyclerList!!.adapter = mCCAsWeekListAdapter
        weekRecyclerList!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                for (i in CCADetailModelArrayList!!.indices) {
                    if (AppController.weekList!!.get(position).getWeekDay().equals(
                            CCADetailModelArrayList!!.get(i).getDay()
                        )
                    ) {
                        pos = i
                      ccaDetailpos = i
                        weekSelected = true
                        break
                    } else {
                        weekSelected = false
                    }
                    if (weekSelected) {
                        break
                    }
                }
                if (!weekSelected) {
                    textViewCCAaSelect!!.visibility = View.GONE
                    TVselectedForWeek!!.visibility = View.GONE
                  msgRelative!!.setVisibility(View.GONE)
                    val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0)
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                    AppController.weekList!!.get(position).setChoiceStatus("2")
                    AppController.weekList!!.get(position).setChoiceStatus1("2")
                    Toast.makeText(mContext, "CCA choice not available", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    textViewCCAaSelect!!.visibility = View.VISIBLE
                    TVselectedForWeek!!.visibility = View.VISIBLE
                    msgRelative!!.setVisibility(View.VISIBLE)
                    val mCCAsActivityAdapter = CCAsActivityAdapter(
                        mContext,
                        CCADetailModelArrayList!!.get(pos)
                            .getCcaChoiceModel(),
                        CCADetailModelArrayList!!.get(pos)
                            .getCcaChoiceModel2(),
                        position,
                        AppController.weekList,
                        weekRecyclerList,ccaedit,CCADetailModelArrayList,nextBtn, submitBtn, filled,ccaDetailpos,msgRelative
                    )
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                }
                for (j in 0 until AppController.weekList!!.size) {
                    if (AppController.weekList!!.get(j).getChoiceStatus()
                            .equals("0") || AppController.weekList!!.get(j)
                            .getChoiceStatus1().equals("0")
                    ) {
                        filled = false
                        break
                    } else {
                        filled = true
                    }
                    if (!filled) {
                        break
                    }
                }
                if (filled) {
                    submitBtn!!.background.alpha = 255
                    submitBtn!!.setVisibility(View.VISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.GONE)
                    AppController.filledFlag = 1
                } else {
                    submitBtn!!.getBackground().setAlpha(150)
                    submitBtn!!.setVisibility(View.INVISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.VISIBLE)
                }
                weekPosition = position
                mCCAsWeekListAdapter =
                    CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition,msgRelative)
                weekRecyclerList!!.adapter = mCCAsWeekListAdapter
                TVselectedForWeek!!.setText(AppController.weekList!!.get(position).getWeekDay())
                //                        horizontalScrollView
                if (weekPosition == 6) weekRecyclerList!!.scrollToPosition(6) else weekRecyclerList!!.scrollToPosition(
                    0
                )
            }

        })


        for (j in 0 until AppController.weekList!!.size) {
            if (AppController.weekList!!.get(j).getDataInWeek().equals("1")) {
                for (i in CCADetailModelArrayList!!.indices) {
                    if (AppController.weekList!!.get(j).getWeekDay().equals(
                            CCADetailModelArrayList!!.get(i).getDay()
                        )
                    ) {
                        pos = i
                        ccaDetailpos = i
                        weekSelected = true
                        break
                    } else {
                        weekSelected = false
                    }
                    if (weekSelected) {
                        break
                    }
                }
                if (!weekSelected) {
                    textViewCCAaSelect!!.visibility = View.GONE
                    TVselectedForWeek!!.visibility = View.GONE
                    msgRelative!!.setVisibility(View.VISIBLE)
                    val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0)
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                    AppController.weekList!!.get(j).setChoiceStatus("2")
                    AppController.weekList!!.get(j).setChoiceStatus1("2")
                    Toast.makeText(mContext, "CCA choice not available", Toast.LENGTH_SHORT).show()
                } else {
                    textViewCCAaSelect!!.visibility = View.VISIBLE
                    TVselectedForWeek!!.visibility = View.VISIBLE
                    msgRelative!!.setVisibility(View.VISIBLE)
                    val mCCAsActivityAdapter = CCAsActivityAdapter(
                        mContext,
                        CCADetailModelArrayList!!.get(pos).getCcaChoiceModel(),
                        CCADetailModelArrayList!!.get(pos).getCcaChoiceModel2(),
                        j,
                        AppController.weekList,
                        weekRecyclerList,ccaedit,CCADetailModelArrayList,nextBtn, submitBtn, filled,ccaDetailpos,msgRelative
                    )
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                }
                for (k in 0 until AppController.weekList!!.size) {
                    if (AppController.weekList!!.get(k).getChoiceStatus()
                            .equals("0") || AppController.weekList!!.get(k)
                            .getChoiceStatus1().equals("0")
                    ) {
                        filled = false
                        msgRelative!!.setVisibility(View.VISIBLE)
                        break
                    } else {
                        filled = true
                    }
                    if (!filled) {
                        break
                    }
                }
                if (filled) {
                    submitBtn!!.getBackground().setAlpha(255)
                    submitBtn!!.setVisibility(View.VISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.GONE)
                    msgRelative!!.setVisibility(View.GONE)
                    AppController.filledFlag = 1
//                    msgRelative.setVisibility(View.GONE);
                } else {
                    msgRelative!!.setVisibility(View.VISIBLE)
                    submitBtn!!.getBackground().setAlpha(150)
                    submitBtn!!.setVisibility(View.INVISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.VISIBLE)
                }
                weekPosition = j
                mCCAsWeekListAdapter =
                    CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition, msgRelative)
                weekRecyclerList!!.adapter = mCCAsWeekListAdapter
                TVselectedForWeek!!.setText(AppController.weekList!!.get(j).getWeekDay())
                break
            }
        }

        if (AppController.weekListWithData!!.size > 0) {
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.VISIBLE)
        } else {
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.GONE)
        }

        nextBtn!!.setOnClickListener(View.OnClickListener {
            Log.e("filled next",filled.toString())
            weekPosition = weekPosition + 1
            if (AppController.weekListWithData!!.contains(weekPosition)) {
                for (a in 0 until AppController.weekListWithData!!.size) {
                    if (AppController.weekListWithData!!.get(a) === weekPosition) {
                        //weekPosition = a;
                        weekPosition = AppController.weekListWithData!!.get(a)
                        break
                    }
                }

                /*           for (int a=0;a<AppController.weekListWithData.size();a++)
                        {
                            if (weekPosition==AppController.weekListWithData.get(a)) {
                                weekPosition = AppController.weekListWithData.get(a);
                            }
                        }
                        weekPosition = AppController.weekListWithData.get(weekPosition);*/
            } else {
                if (weekPosition >= AppController.weekList!!.size - 1) {
                    weekPosition = 0
                }
                if (AppController.weekListWithData!!.contains(weekPosition)) {
                    //                        weekPosition = AppController.weekListWithData.get(weekPosition);
                    for (a in 0 until AppController.weekListWithData!!.size) {
                        //                            if (AppController.weekListWithData.contains(weekPosition)) {
                        if (AppController.weekListWithData!!.get(a) === weekPosition) {
                            //                                weekPosition = a;
                            weekPosition = AppController.weekListWithData!!.get(a)
                            break
                        }
                    }
                } else {
                    for (m in weekPosition until AppController.weekList!!.size) {
                        if (AppController.weekListWithData!!.contains(m)) {
                            weekPosition = m
                            println("weekposition:m:$weekPosition")
                            break
                        }
                    }
                    if (!AppController.weekListWithData!!.contains(weekPosition)) {
                        weekPosition = 0
                    }
                }
            }
            for (j in weekPosition until AppController.weekList!!.size) {
                if (AppController.weekList!!.get(j).getDataInWeek().equals("1")) {
                    for (i in CCADetailModelArrayList!!.indices) {
                        if (AppController.weekList!!.get(j).getWeekDay().equals(
                                CCADetailModelArrayList!!.get(i).getDay(),ignoreCase = true
                            )
                        ) {
                            pos = i
                            ccaDetailpos = i
                            weekSelected = true
                            break
                        } else {
                            weekSelected = false
                        }
                        if (weekSelected) {
                            break
                        }
                    }
                    if (!weekSelected) {
                        textViewCCAaSelect!!.visibility = View.GONE
                        TVselectedForWeek!!.visibility = View.GONE
                        val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0)
                        recycler_review!!.adapter = mCCAsActivityAdapter
                        mCCAsActivityAdapter.notifyDataSetChanged()
                        AppController.weekList!!.get(j).choiceStatus = "2"
                        AppController.weekList!!.get(j).choiceStatus1 = "2"
                        //                            Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();
                    } else {
                        textViewCCAaSelect!!.visibility = View.VISIBLE
                        TVselectedForWeek!!.visibility = View.VISIBLE
                        val mCCAsActivityAdapter = CCAsActivityAdapter(
                            mContext,
                            CCADetailModelArrayList!!.get(pos)
                                .getCcaChoiceModel(),
                            CCADetailModelArrayList!!.get(pos)
                                .getCcaChoiceModel2(),
                            j,
                            AppController.weekList,
                            weekRecyclerList,ccaedit,CCADetailModelArrayList,nextBtn, submitBtn, filled,ccaDetailpos,msgRelative
                        )
                        recycler_review!!.adapter = mCCAsActivityAdapter
                        mCCAsActivityAdapter.notifyDataSetChanged()
                    }
                    for (k in 0 until AppController.weekList!!.size) {
                        if (AppController.weekList!!.get(k).getChoiceStatus()
                                .equals("0") || AppController.weekList!!.get(k)
                                .getChoiceStatus1().equals("0",ignoreCase = true)
                        ) {
                            filled = false
                            msgRelative!!.setVisibility(View.VISIBLE)
                            break
                        } else {
                            filled = true
                        }
                        if (!filled) {
                            break
                        }
                    }
                    if (filled) {
                        submitBtn!!.getBackground().setAlpha(255)
                        submitBtn!!.setVisibility(View.VISIBLE)
                        nextBtn!!.getBackground().setAlpha(255)
                        nextBtn!!.setVisibility(View.GONE)
                        msgRelative!!.setVisibility(View.GONE)
                        nextBtn!!.setVisibility(View.GONE)
                        AppController.filledFlag = 1
                    } else {
                        msgRelative!!.setVisibility(View.VISIBLE)
                        submitBtn!!.getBackground().setAlpha(150)
                        submitBtn!!.setVisibility(View.INVISIBLE)
                        nextBtn!!.getBackground().setAlpha(255)
                        nextBtn!!.setVisibility(View.VISIBLE)
                    }
                    weekPosition = j
                    mCCAsWeekListAdapter =
                        CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition,msgRelative)
                    weekRecyclerList!!.adapter = mCCAsWeekListAdapter
                    TVselectedForWeek!!.text = AppController.weekList!![j].weekDay
                    break
                }
            }
            if (weekPosition == 6) {
                weekRecyclerList!!.layoutManager!!.scrollToPosition(weekPosition)
            } else {
                weekRecyclerList!!.layoutManager!!.scrollToPosition(0)
            }
        })
    }
    fun getWeekday(weekDay: Int): String? {
        var day = ""
        when (weekDay) {
            0 -> day = "Sunday"
            1 -> day = "Monday"
            2 -> day = "Tuesday"
            3 -> day = "Wednesday"
            4 -> day = "Thursday"
            5 -> day = "Friday"
            6 -> day = "Saturday"
        }
        return day
    }

    fun getWeekdayMMM(weekDay: Int): String? {
        var day = ""
        when (weekDay) {
            0 -> day = "SUN"
            1 -> day = "MON"
            2 -> day = "TUE"
            3 -> day = "WED"
            4 -> day = "THU"
            5 -> day = "FRI"
            6 -> day = "SAT"
        }
        return day
    }


}