package com.nas.naisak.activity.cca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.CCAAttendanceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAAttendenceListAdapter extends  RecyclerView.Adapter<CCAAttendenceListAdapter.MyViewHolder> {
    ArrayList<CCAAttendanceModel> mSocialMediaModels;
    Context mContext;
    int mPosition=0;
    int mChoiceStatus=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ccaDate;
        TextView ccaDateStatus;

        public MyViewHolder(View view) {
            super(view);

            ccaDate= (TextView) view.findViewById(R.id.ccaDate);
            ccaDateStatus= (TextView) view.findViewById(R.id.ccaDateStatus);


        }
    }
    public CCAAttendenceListAdapter(Context mContext, ArrayList<CCAAttendanceModel> mSocialMediaModels) {
        this.mContext = mContext;
      this.mSocialMediaModels=mSocialMediaModels;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_review_attendancelist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.ccaDate.setText(dateParsingTodd_MMM_yyyy(mSocialMediaModels.get(position).getDateAttend()));
        if (mSocialMediaModels.get(position).getStatusCCA().equalsIgnoreCase("u"))
        {
            holder.ccaDateStatus.setText("Upcoming");
            holder.ccaDateStatus.setTextColor(mContext.getResources().getColor(R.color.rel_six));

        }
        else  if (mSocialMediaModels.get(position).getStatusCCA().equalsIgnoreCase("p"))
        {
            holder.ccaDateStatus.setText("Present");
            holder.ccaDateStatus.setTextColor(mContext.getResources().getColor(R.color.nas_green));


        } else  if (mSocialMediaModels.get(position).getStatusCCA().equalsIgnoreCase("a"))
        {
            holder.ccaDateStatus.setText("Absent");
            holder.ccaDateStatus.setTextColor(mContext.getResources().getColor(R.color.rel_nine));

        }


    }


    @Override
    public int getItemCount() {


        return(mSocialMediaModels.size());
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
