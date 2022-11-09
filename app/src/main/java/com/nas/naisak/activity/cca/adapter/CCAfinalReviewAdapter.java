package com.nas.naisak.activity.cca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.CCADetailModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAfinalReviewAdapter extends  RecyclerView.Adapter<CCAfinalReviewAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CCADetailModel>mCCADetailModelArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCCAaDateItemChoice1;
        TextView textViewCCAaDateItemChoice2;

        TextView textViewCCADay;
        TextView textViewCCAChoice1;
        TextView textViewCCAChoice2;
        TextView locationTxt;
        TextView location2Txt;
        TextView description2Txt;
        TextView descriptionTxt;
        LinearLayout linearChoice1,linearChoice2;
        public MyViewHolder(View view) {
            super(view);

            textViewCCAaDateItemChoice1= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice1);
            textViewCCAaDateItemChoice2= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice2);
            textViewCCADay= (TextView) view.findViewById(R.id.textViewCCADay);
            textViewCCAChoice1= (TextView) view.findViewById(R.id.textViewCCAChoice1);
            textViewCCAChoice2= (TextView) view.findViewById(R.id.textViewCCAChoice2);
            locationTxt= (TextView) view.findViewById(R.id.locationTxt);
            location2Txt= (TextView) view.findViewById(R.id.location2Txt);
            description2Txt= (TextView) view.findViewById(R.id.description2Txt);
            descriptionTxt= (TextView) view.findViewById(R.id.descriptionTxt);
            linearChoice1= (LinearLayout) view.findViewById(R.id.linearChoice1);
            linearChoice2= (LinearLayout) view.findViewById(R.id.linearChoice2);


        }
    }

//    public CCAfinalReviewAdapter(Context mContext) {
//        this.mContext = mContext;
//    }
//    public CCAfinalReviewAdapter(Context mContext,ArrayList<CCADetailModel>mCCADetailModelArrayList) {
//        this.mContext = mContext;
//        this.mCCADetailModelArrayList = mCCADetailModelArrayList;
//    }
public CCAfinalReviewAdapter(Context mContext, ArrayList<CCADetailModel>mCCADetailModelArrayList) {
    this.mContext = mContext;
    this.mCCADetailModelArrayList = mCCADetailModelArrayList;
}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_final_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



        if (mCCADetailModelArrayList.get(position).getLocation().equalsIgnoreCase("0")|| mCCADetailModelArrayList.get(position).getLocation().equalsIgnoreCase(""))
        {
            holder.locationTxt.setVisibility(View.GONE);
        }
        else
        {
            holder.locationTxt.setVisibility(View.VISIBLE);
            holder.locationTxt.setText("Location       : "+mCCADetailModelArrayList.get(position).getLocation());
        }
        if (mCCADetailModelArrayList.get(position).getLocation2().equalsIgnoreCase("0")|| mCCADetailModelArrayList.get(position).getLocation2().equalsIgnoreCase(""))
        {
            holder.location2Txt.setVisibility(View.GONE);
        }
        else
        {
            holder.location2Txt.setVisibility(View.VISIBLE);
            holder.location2Txt.setText("Location       : "+mCCADetailModelArrayList.get(position).getLocation2());
        }

        if (mCCADetailModelArrayList.get(position).getDescription().equalsIgnoreCase("0")|| mCCADetailModelArrayList.get(position).getDescription().equalsIgnoreCase(""))
        {
            holder.descriptionTxt.setVisibility(View.GONE);
        }
        else
        {
            holder.descriptionTxt.setVisibility(View.VISIBLE);
            holder.descriptionTxt.setText("Description : "+mCCADetailModelArrayList.get(position).getDescription());
        }
        if (mCCADetailModelArrayList.get(position).getDescription2().equalsIgnoreCase("0")|| mCCADetailModelArrayList.get(position).getDescription2().equalsIgnoreCase(""))
        {
            holder.description2Txt.setVisibility(View.GONE);
        }
        else
        {
            holder.description2Txt.setVisibility(View.VISIBLE);
            holder.description2Txt.setText("Description : "+mCCADetailModelArrayList.get(position).getDescription2());
        }
    holder.textViewCCADay.setText(mCCADetailModelArrayList.get(position).getDay());
        if (mCCADetailModelArrayList.get(position).getChoice1()==null)
        {
            holder.linearChoice1.setVisibility(View.GONE);
            holder.textViewCCAChoice1.setText("Choice 1 : Nil");

        }else
        {
            holder.linearChoice1.setVisibility(View.VISIBLE);

            holder.textViewCCAChoice1.setText("Choice 1 : "+mCCADetailModelArrayList.get(position).getChoice1());


            if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.GONE);

            }

        }
        if (mCCADetailModelArrayList.get(position).getChoice2()==null)
        {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : Nil");

        }else
        {
            holder.linearChoice2.setVisibility(View.VISIBLE);

            holder.textViewCCAChoice2.setText("Choice 2 : "+mCCADetailModelArrayList.get(position).getChoice2());
            if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.GONE);

            }
        }



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
    public int getItemCount() {

        Log.e("size", String.valueOf(mCCADetailModelArrayList.size()));
        return mCCADetailModelArrayList.size();
    }
}
