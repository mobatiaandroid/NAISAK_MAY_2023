package com.nas.naisak.activity.cca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.CCADetailModel;
import com.nas.naisak.activity.cca.model.CCAchoiceModel;
import com.nas.naisak.activity.cca.model.WeekListModel;
import com.nas.naisak.constants.AppController;
import com.nas.naisak.constants.CommonMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAsChoiceListActivityAdapter extends RecyclerView.Adapter<CCAsChoiceListActivityAdapter.MyViewHolder> {
    //    ArrayList<String> mSocialMediaModels;
    ArrayList<CCAchoiceModel> mCCAmodelArrayList;
    Context mContext;
    int dayPosition = 0;
    int choicePosition = 0;
    int ccaDetailpos = 0;
    RelativeLayout msgRelative;
    ArrayList<WeekListModel> weekList;
    ArrayList<CCADetailModel> ccaDetailModelArrayList;
    Button submitBtn, nextBtn;
    Boolean filled;
    RecyclerView recyclerWeek;


    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList,
                                         int mdayPosition, ArrayList<WeekListModel> mWeekList,
                                         int mChoicePosition, RecyclerView recyclerWeek,
                                         ArrayList<CCADetailModel> ccaDetailModelArrayList,
                                         Button submitBtn, Button nextBtn, Boolean filled, int ccaDetailpos, RelativeLayout msgRelative) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;
        this.choicePosition = mChoicePosition;
        this.recyclerWeek = recyclerWeek;
        this.ccaDetailModelArrayList = ccaDetailModelArrayList;
        this.submitBtn = submitBtn;
        this.nextBtn = nextBtn;
        this.filled = filled;
        this.ccaDetailpos = ccaDetailpos;
        this.msgRelative = msgRelative;
    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList, int mdayPosition, ArrayList<WeekListModel> mWeekList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;

    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList, int mdayPosition, ArrayList<WeekListModel> mWeekList, int mChoicePosition, RecyclerView recyclerWeek) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;
        this.choicePosition = mChoicePosition;
        this.recyclerWeek = recyclerWeek;

    }

    public static String convertTimeToAMPM(String date) {
        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ccalist_activity_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.confirmationImageview.setVisibility(View.VISIBLE);
        if (mCCAmodelArrayList.get(position).getVenue() != null) {
            if (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase("0") || (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase(""))) {
                holder.textViewCCAVenue.setVisibility(View.GONE);
            } else {
                holder.textViewCCAVenue.setText(String.format("Location: %s", mCCAmodelArrayList.get(position).getVenue()));
                holder.textViewCCAVenue.setVisibility(View.VISIBLE);

            }
        } else {
            holder.textViewCCAVenue.setVisibility(View.GONE);
        }

//
//        System.out.println("DESC TEST"+mCCAmodelArrayList.get(position).getDescription());
//
        if (mCCAmodelArrayList.get(position).getDescription() != null) {
            if (mCCAmodelArrayList.get(position).getDescription().equalsIgnoreCase("0") || mCCAmodelArrayList.get(position).getDescription().equalsIgnoreCase("")) {
                holder.descriptionRel.setVisibility(View.GONE);
            } else {
                holder.descriptionRel.setVisibility(View.VISIBLE);
                holder.descriptionTxt.setText(String.format("Description : %s", mCCAmodelArrayList.get(position).getDescription()));
                if (mCCAmodelArrayList.get(position).getDescription().length() > 22) {
                    holder.readMoreTxt.setVisibility(View.VISIBLE);
                } else {
                    holder.readMoreTxt.setVisibility(View.GONE);
                }

                holder.readMoreTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonMethods.Companion.showDialogueWithOk(mContext, mCCAmodelArrayList.get(position).getDescription(), "Description");

                    }
                });
            }
        } else {
            holder.descriptionRel.setVisibility(View.GONE);
        }
//        //   Log.e("DESC ADA",mCCAmodelArrayList.get(position).getDescription());
//
//        Integer count=holder.descriptionTxt.getLineCount();
//        Log.e("LINE COUNT", String.valueOf(count));
//
//
        if (choicePosition == 0) {

            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));

            } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));


            } else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
                AppController.weekList.get(dayPosition).setChoiceStatus("1");
                ccaDetailModelArrayList.get(ccaDetailpos).setChoice1(mCCAmodelArrayList.get(position).getCca_item_name());
                ccaDetailModelArrayList.get(ccaDetailpos).setChoice1Id(mCCAmodelArrayList.get(position).getCca_details_id());
                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition, msgRelative);
                mCCAsWeekListAdapter.notifyDataSetChanged();
                recyclerWeek.setAdapter(mCCAsWeekListAdapter);

            }
        } else {

            System.out.println("disable2::" + mCCAmodelArrayList.get(position).getDisableCccaiem() + " @ " + position);
            System.out.println("disable2::" + mCCAmodelArrayList.get(position).getDisableCccaiem() + " @dayPosition: " + dayPosition);
            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));

            } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
                AppController.weekList.get(dayPosition).setChoiceStatus1("1");
                ccaDetailModelArrayList.get(ccaDetailpos).setChoice2(mCCAmodelArrayList.get(position).getCca_item_name());
                ccaDetailModelArrayList.get(ccaDetailpos).setChoice2Id(mCCAmodelArrayList.get(position).getCca_details_id());
                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition, msgRelative);
                mCCAsWeekListAdapter.notifyDataSetChanged();
                recyclerWeek.setAdapter(mCCAsWeekListAdapter);

            }
        }

        for (int j = 0; j < AppController.weekList.size(); j++) {
            if (AppController.weekList.get(j).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(j).getChoiceStatus1().equalsIgnoreCase("0")) {
                filled = false;
                break;
            } else {
                filled = true;
            }
            if (!(filled)) {
                break;

            }

        }
        if (filled) {
            submitBtn.getBackground().setAlpha(255);
            submitBtn.setVisibility(View.VISIBLE);
            nextBtn.getBackground().setAlpha(255);
            nextBtn.setVisibility(View.GONE);

        } else {
            submitBtn.getBackground().setAlpha(150);
            submitBtn.setVisibility(View.INVISIBLE);
            nextBtn.getBackground().setAlpha(255);
            nextBtn.setVisibility(View.VISIBLE);

        }
        holder.listTxtView.setText(mCCAmodelArrayList.get(position).getCca_item_name());
        if (mCCAmodelArrayList.get(position).getCca_item_start_time() != null && mCCAmodelArrayList.get(position).getCca_item_end_time() != null) {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("(" + convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time()) + " - " + convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time()) + ")");

        } else if (mCCAmodelArrayList.get(position).getCca_item_start_time() != null) {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("(" + convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time()) + ")");
        } else if (mCCAmodelArrayList.get(position).getCca_item_end_time() != null) {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("(" + convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time()) + ")");
        } else {
            holder.textViewCCAaDateItem.setVisibility(View.GONE);

        }
//        holder.confirmationImageview.setVisibility(View.VISIBLE);
//        if (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase("0")|| (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase("")))
//        {
//            holder.textViewCCAVenue.setVisibility(View.GONE);
//
//        }
//        else
//        {
//
//            holder.textViewCCAVenue.setText(mCCAmodelArrayList.get(position).getVenue());
//            holder.textViewCCAVenue.setVisibility(View.VISIBLE);
//
//        }
//        if (choicePosition == 0) {
//
//            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
//                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));
//
//            }else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
//                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
//
//
//            } else {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
//                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
//                AppController.weekList.get(dayPosition).setChoiceStatus("1");
//                ccaDetailModelArrayList.get(ccaDetailpos).setChoice1(mCCAmodelArrayList.get(position).getCca_item_name());
//                ccaDetailModelArrayList.get(ccaDetailpos).setChoice1Id(mCCAmodelArrayList.get(position).getCca_details_id());
//                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition,msgRelative);
//                mCCAsWeekListAdapter.notifyDataSetChanged();
//                recyclerWeek.setAdapter(mCCAsWeekListAdapter);
//
//            }
//        } else {
//
//            System.out.println("disable2::"+mCCAmodelArrayList.get(position).getDisableCccaiem()+" @ "+position);
//            System.out.println("disable2::"+mCCAmodelArrayList.get(position).getDisableCccaiem()+" @dayPosition: "+dayPosition);
//            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
//                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));
//
//            }else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
//                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
//
//
//
//            } else {
//                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
//                AppController.weekList.get(dayPosition).setChoiceStatus1("1");
//                ccaDetailModelArrayList.get(ccaDetailpos).setChoice2(mCCAmodelArrayList.get(position).getCca_item_name());
//                ccaDetailModelArrayList.get(ccaDetailpos).setChoice2Id(mCCAmodelArrayList.get(position).getCca_details_id());
//                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition,msgRelative);
//                mCCAsWeekListAdapter.notifyDataSetChanged();
//                recyclerWeek.setAdapter(mCCAsWeekListAdapter);
//
//            }
//        }
//
//        for (int j = 0; j < AppController.weekList.size(); j++) {
//            if (AppController.weekList.get(j).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(j).getChoiceStatus1().equalsIgnoreCase("0")) {
//                filled = false;
//                break;
//            } else {
//                filled = true;
//                AppController.Companion.setFilledFlag(1);
//            }
//            if (!( filled)) {
//                break;
//            }
//
//        }
//        if (filled) {
//            submitBtn.getBackground().setAlpha(255);
//            submitBtn.setVisibility(View.VISIBLE);
//            nextBtn.getBackground().setAlpha(255);
//            nextBtn.setVisibility(View.GONE);
//
//        } else {
//            submitBtn.getBackground().setAlpha(150);
//            submitBtn.setVisibility(View.INVISIBLE);
//            nextBtn.getBackground().setAlpha(255);
//            nextBtn.setVisibility(View.VISIBLE);
//
//        }
////        if(mCCAmodelArrayList.get(position).getDescription().length()>22)
////        {
////            holder.readMoreTxt.setVisibility(View.GONE);
////        }
////        else
////        {
////            holder.readMoreTxt.setVisibility(View.GONE);
////        }
//        holder.readMoreTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonMethods.Companion.showDialogueWithOk(mContext,mCCAmodelArrayList.get(position).getDescription(),"Description");
//
//
//            }
//        });
//        holder.listTxtView.setText(mCCAmodelArrayList.get(position).getCca_item_name());
//        if (mCCAmodelArrayList.get(position).getCca_item_start_time()!=null && mCCAmodelArrayList.get(position).getCca_item_end_time()!=null)
//        {
//            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);
//
//            holder.textViewCCAaDateItem.setText("("+convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time())
//                    +" - "+convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time())+")");
//
//        }else if (mCCAmodelArrayList.get(position).getCca_item_start_time()!=null)
//        {
//            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);
//
//            holder.textViewCCAaDateItem.setText("("+convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time())+")");
//        }else if (mCCAmodelArrayList.get(position).getCca_item_end_time()!=null)
//        {
//            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);
//
//            holder.textViewCCAaDateItem.setText("("+convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time())+")");
//        }
//        else
//        {
//            holder.textViewCCAaDateItem.setVisibility(View.GONE);
//
//        }


    }

    @Override
    public int getItemCount() {

        return mCCAmodelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        TextView textViewCCAaDateItem;
        ImageView confirmationImageview;
        TextView textViewCCAVenue;
        TextView descriptionTxt;
        TextView readMoreTxt;
        RelativeLayout descriptionRel;

        public MyViewHolder(View view) {
            super(view);
            textViewCCAaDateItem = (TextView) view.findViewById(R.id.textViewCCAaDateItem);
            listTxtView = (TextView) view.findViewById(R.id.textViewCCAaItem);
            textViewCCAVenue = (TextView) view.findViewById(R.id.textViewCCAVenue);
            descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxt);
            readMoreTxt = (TextView) view.findViewById(R.id.readMoreTxt);
            confirmationImageview = (ImageView) view.findViewById(R.id.confirmationImageview);
            descriptionRel = (RelativeLayout) view.findViewById(R.id.descriptionRel);


        }
    }
}
