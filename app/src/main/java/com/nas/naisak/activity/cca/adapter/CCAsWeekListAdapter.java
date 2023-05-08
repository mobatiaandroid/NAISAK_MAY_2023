package com.nas.naisak.activity.cca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.WeekListModel;
import com.nas.naisak.constants.AppController;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAsWeekListAdapter extends RecyclerView.Adapter<CCAsWeekListAdapter.MyViewHolder> {
    ArrayList<String> mCCAmodelArrayList;
    ArrayList<WeekListModel> mWeekListModelArrayList;
    Context mContext;
    int weekPosition = 0;
    RelativeLayout msgRelative;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        View selectionCompletedView;
        ImageView weekSelectedImageView;
        LinearLayout linearBg;
        LinearLayout linearChoice;

        public MyViewHolder(View view) {
            super(view);

            listTxtView = (TextView) view.findViewById(R.id.textViewCCAaItem);
            selectionCompletedView = (View) view.findViewById(R.id.selectionCompletedView);
            weekSelectedImageView = (ImageView) view.findViewById(R.id.weekSelectedImageView);
            linearBg = (LinearLayout) view.findViewById(R.id.linearBg);
            linearChoice = (LinearLayout) view.findViewById(R.id.linearChoice);


        }
    }

    public CCAsWeekListAdapter(Context mContext, ArrayList<WeekListModel> mCCAmodelArrayList) {
        this.mContext = mContext;
        this.mWeekListModelArrayList = mCCAmodelArrayList;
    }

    public CCAsWeekListAdapter(Context mContext, ArrayList<WeekListModel> mCCAmodelArrayList, int mWeekPosition, RelativeLayout msgRelative) {
        this.mContext = mContext;
        this.mWeekListModelArrayList = mCCAmodelArrayList;
        this.weekPosition = mWeekPosition;
        this.msgRelative= msgRelative;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_weeklist_cca, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtView.setText(mWeekListModelArrayList.get(position).getWeekDayMMM());
        if (AppController.weekList.get(position).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(position).getChoiceStatus1().equalsIgnoreCase("0")) {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_pending);
            holder.linearBg.setBackgroundResource(R.color.white);
            holder.linearChoice.setBackgroundResource(R.color.white);

        } else if (AppController.weekList.get(position).getChoiceStatus().equalsIgnoreCase("2") && AppController.weekList.get(position).getChoiceStatus1().equalsIgnoreCase("2")) {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_not_available);
            holder.linearBg.setBackgroundResource(R.color.light_grey);
            holder.linearChoice.setBackgroundResource(R.color.light_grey);

        } else {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_completed);

            holder.linearBg.setBackgroundResource(R.color.white);
            holder.linearChoice.setBackgroundResource(R.color.white);

        }
        if (weekPosition == position) {
            holder.weekSelectedImageView.setVisibility(View.VISIBLE);
            if (AppController.weekList.get(position).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(position).getChoiceStatus1().equalsIgnoreCase("0")) {
                msgRelative.setVisibility(View.VISIBLE);

            } else if (AppController.weekList.get(position).getChoiceStatus().equalsIgnoreCase("2") && AppController.weekList.get(position).getChoiceStatus1().equalsIgnoreCase("2")) {
                msgRelative.setVisibility(View.GONE);

            } else {
                msgRelative.setVisibility(View.GONE);

            }
        } else {
            holder.weekSelectedImageView.setVisibility(View.INVISIBLE);

        }


    }


    @Override
    public int getItemCount() {
        return mWeekListModelArrayList.size();
    }
}
