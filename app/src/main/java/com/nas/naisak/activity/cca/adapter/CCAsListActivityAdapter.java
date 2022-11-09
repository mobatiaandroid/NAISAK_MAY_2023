package com.nas.naisak.activity.cca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.CCAModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CCAsListActivityAdapter extends RecyclerView.Adapter<CCAsListActivityAdapter.MyViewHolder> {
    ArrayList<CCAModel> mCCAmodelArrayList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        TextView listTxtViewDate;
        TextView status;
        ImageView statusImageView;
        RelativeLayout statusLayout;

        public MyViewHolder(View view) {
            super(view);

            listTxtView = view.findViewById(R.id.textViewCCAaItem);
            listTxtViewDate = view.findViewById(R.id.textViewCCAaDateItem);
            statusImageView = view.findViewById(R.id.statusImageView);
            statusLayout = view.findViewById(R.id.statusLayout);
            status = view.findViewById(R.id.status);


        }
    }

    public CCAsListActivityAdapter(Context mContext, ArrayList<CCAModel> mCCAmodelArrayList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_first_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtView.setText(mCCAmodelArrayList.get(position).getTitle());
        String fromDate,toDate;

        holder.listTxtViewDate.setText(dateParsingTodd_MMM_yyyy(mCCAmodelArrayList.get(position).getFrom_date()) + " to " + dateParsingTodd_MMM_yyyy(mCCAmodelArrayList.get(position).getTo_date()));

        if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("0")) {
            if (mCCAmodelArrayList.get(position).getIsSubmissionDateOver().equalsIgnoreCase("1")) {
                //closed
                holder.statusImageView.setImageResource(R.drawable.closed);
            } else {
                holder.statusImageView.setImageResource(R.drawable.edit);//edit
            }
        } else if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("1")) {
            //approved
            holder.statusImageView.setImageResource(R.drawable.approve_new);

        } else if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("2")) {
            //pending
            holder.statusImageView.setImageResource(R.drawable.pending);

        }
//        if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
//            holder.statusLayout.setVisibility(View.VISIBLE);
//            holder.status.setBackgroundResource(R.drawable.rectangle_red);
//            holder.status.setText("New");
//        } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("1") || mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("")) {
//            holder.statusLayout.setVisibility(View.INVISIBLE);
//
//        } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
//            holder.statusLayout.setVisibility(View.VISIBLE);
//            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
//            holder.status.setText("Updated");
//        }

    }


    @Override
    public int getItemCount() {
        Log.e("size", String.valueOf(mCCAmodelArrayList.size()));
        return mCCAmodelArrayList.size();
    }
    public static String dateParsingTodd_MMM_yyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
}
