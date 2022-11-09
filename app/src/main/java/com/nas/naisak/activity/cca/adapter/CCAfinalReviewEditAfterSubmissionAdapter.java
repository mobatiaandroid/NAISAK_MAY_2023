package com.nas.naisak.activity.cca.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.cca.model.CCAReviewAfterSubmissionModel;
import com.nas.naisak.constants.CommonMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAfinalReviewEditAfterSubmissionAdapter extends RecyclerView.Adapter<CCAfinalReviewEditAfterSubmissionAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CCAReviewAfterSubmissionModel> mCCADetailModelArrayList;
    Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCCADay;
        TextView textViewCCAChoice1;
        TextView textViewCCAChoice2;
        ImageView attendanceListIcon;
        ImageView deleteChoice1;
        ImageView deleteChoice2;
        LinearLayout linearChoice1, linearChoice2;
        TextView textViewCCAaDateItemChoice1;
        TextView textViewCCAaDateItemChoice2;
        TextView locationTxt;
        TextView descriptionTxt;
        TextView location2Txt;
        TextView description2Txt;
        TextView readMore,readMore1;
        public MyViewHolder(View view) {
            super(view);
            textViewCCAaDateItemChoice1= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice1);
            textViewCCAaDateItemChoice2= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice2);
            textViewCCADay = (TextView) view.findViewById(R.id.textViewCCADay);
            textViewCCAChoice1 = (TextView) view.findViewById(R.id.textViewCCAChoice1);
            textViewCCAChoice2 = (TextView) view.findViewById(R.id.textViewCCAChoice2);
            locationTxt = (TextView) view.findViewById(R.id.locationTxt);
            descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxt);
            description2Txt = (TextView) view.findViewById(R.id.description2Txt);
            location2Txt = (TextView) view.findViewById(R.id.location2Txt);
            readMore = (TextView) view.findViewById(R.id.readMore);
            readMore1 = (TextView) view.findViewById(R.id.readMore1);
            attendanceListIcon = (ImageView) view.findViewById(R.id.attendanceListIcon);
            deleteChoice1 = (ImageView) view.findViewById(R.id.deleteChoice1);
            deleteChoice2 = (ImageView) view.findViewById(R.id.deleteChoice2);
            linearChoice1 = (LinearLayout) view.findViewById(R.id.linearChoice1);
            linearChoice2 = (LinearLayout) view.findViewById(R.id.linearChoice2);


        }
    }

    public CCAfinalReviewEditAfterSubmissionAdapter(Context mContext, ArrayList<CCAReviewAfterSubmissionModel> mCCADetailModelArrayList) {
        this.mContext = mContext;
        this.mCCADetailModelArrayList = mCCADetailModelArrayList;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attendance_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_review_after_submit, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.textViewCCADay.setText(mCCADetailModelArrayList.get(position).getDay());
        holder.attendanceListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) || (!(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
                    showAttendanceList(position);
                }
            }
        });
        if (mCCADetailModelArrayList.get(position).getCca_item_description_2() != null){
            if (mCCADetailModelArrayList.get(position).getCca_item_description_2().equals("")){
                holder.readMore.setVisibility(View.GONE);
            }else{
                holder.readMore.setVisibility(View.VISIBLE);

            }

        }else{
            holder.readMore.setVisibility(View.GONE);
        }
        if (mCCADetailModelArrayList.get(position).getCca_item_description() != null ){
            if ( mCCADetailModelArrayList.get(position).getCca_item_description().equals("")){
                holder.readMore1.setVisibility(View.GONE);

            }else{
                holder.readMore1.setVisibility(View.VISIBLE);

            }
        }else{
            holder.readMore1.setVisibility(View.GONE);
        }

        if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) {
            holder.linearChoice1.setVisibility(View.GONE);

            holder.textViewCCAChoice1.setText("Choice 1 : None");

        } else if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1")) {
            holder.linearChoice1.setVisibility(View.GONE);
            holder.textViewCCAChoice1.setText("Choice 1 : Nil");

        } else {
            holder.linearChoice1.setVisibility(View.VISIBLE);
            holder.textViewCCAChoice1.setText(mCCADetailModelArrayList.get(position).getChoice1());
            if (mCCADetailModelArrayList.get(position).getCca_item_description() != null){
                if (mCCADetailModelArrayList.get(position).getCca_item_description().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getCca_item_description().equalsIgnoreCase(""))
                {
                    holder.descriptionTxt.setVisibility(View.GONE);
                    holder.readMore1.setVisibility(View.GONE);
                }
                else
                {
                    holder.descriptionTxt.setVisibility(View.VISIBLE);
                    holder.readMore1.setVisibility(View.VISIBLE);
                    holder.descriptionTxt.setText("Description      : "+mCCADetailModelArrayList.get(position).getCca_item_description());

                }
            }
            if (mCCADetailModelArrayList.get(position).getVenue() != null){
                if (mCCADetailModelArrayList.get(position).getVenue().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getVenue().equalsIgnoreCase(""))
                {
                    holder.locationTxt.setVisibility(View.GONE);
                }
                else
                {
                    holder.locationTxt.setVisibility(View.VISIBLE);
                    holder.locationTxt.setText("Location            : "+mCCADetailModelArrayList.get(position).getVenue());

                }
            }

            if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+ convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.GONE);

            }

        }
        if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : None");

        } else if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")) {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : Nil");

        } else {
            holder.linearChoice2.setVisibility(View.VISIBLE);
            holder.textViewCCAChoice2.setText( mCCADetailModelArrayList.get(position).getChoice2());
            if (mCCADetailModelArrayList.get(position).getVenue2() != null){
                if (mCCADetailModelArrayList.get(position).getVenue2().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getVenue2().equalsIgnoreCase(""))
                {
                    holder.location2Txt.setVisibility(View.GONE);
                    holder.readMore.setVisibility(View.GONE);
                }
                else
                {
                    holder.location2Txt.setVisibility(View.VISIBLE);
                    holder.readMore.setVisibility(View.VISIBLE);
                    holder.location2Txt.setText("Location            : "+mCCADetailModelArrayList.get(position).getVenue2());

                }
            }

            if (mCCADetailModelArrayList.get(position).getCca_item_description_2() != null){
                if (mCCADetailModelArrayList.get(position).getCca_item_description_2().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getCca_item_description_2().equalsIgnoreCase(""))
                {
                    holder.description2Txt.setVisibility(View.GONE);
                }
                else
                {
                    holder.description2Txt.setVisibility(View.VISIBLE);
                    holder.description2Txt.setText("Description      : "+mCCADetailModelArrayList.get(position).getCca_item_description_2());

                }
            }


            holder.readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCCADetailModelArrayList.get(position).getCca_item_description_2() != null){

                            CommonMethods.Companion.showDialogueWithOk(mContext,mCCADetailModelArrayList.get(position).getCca_item_description_2(),"Description");


                    }else{
                        Toast.makeText(mContext, "No Description available", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        holder.description2Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.Companion.showDialogueWithOk(mContext,mCCADetailModelArrayList.get(position).getCca_item_description_2(),"Description");

            }
        });

        holder.readMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.Companion.showDialogueWithOk(mContext,mCCADetailModelArrayList.get(position).getCca_item_description(),"Description");

            }
        });
        holder.descriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.Companion.showDialogueWithOk(mContext,mCCADetailModelArrayList.get(position).getCca_item_description(),"Description");
            }
        });




            if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+ convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.GONE);

            }

        }
        if (((mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) && ((mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
            holder.attendanceListIcon.setVisibility(View.INVISIBLE);
            holder.deleteChoice1.setVisibility(View.INVISIBLE);
            holder.deleteChoice2.setVisibility(View.INVISIBLE);
        } else {
            holder.attendanceListIcon.setVisibility(View.INVISIBLE);
            holder.deleteChoice1.setVisibility(View.INVISIBLE);
            holder.deleteChoice2.setVisibility(View.INVISIBLE);

        }

//        holder.textViewCCADay.setText(mCCADetailModelArrayList.get(position).getDay());
//        holder.attendanceListIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((!(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) || (!(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
//                    showAttendanceList(position);
//                }
//            }
//        });
//        if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) {
//            holder.linearChoice1.setVisibility(View.GONE);
//
//            holder.textViewCCAChoice1.setText("Choice 1 : None");
//
//        } else if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1")) {
//            holder.linearChoice1.setVisibility(View.GONE);
//            holder.textViewCCAChoice1.setText("Choice 1 : Nil");
//
//        } else {
//            holder.linearChoice1.setVisibility(View.VISIBLE);
//            holder.textViewCCAChoice1.setText(mCCADetailModelArrayList.get(position).getChoice1());
//
//            if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice1.setText("("+ convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
//
//            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+")");
//            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice1.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
//            }
//            else
//            {
//                holder.textViewCCAaDateItemChoice1.setVisibility(View.GONE);
//
//            }
//
//        }
//        if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) {
//            holder.linearChoice2.setVisibility(View.GONE);
//            holder.textViewCCAChoice2.setText("Choice 2 : None");
//
//        } else if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")) {
//            holder.linearChoice2.setVisibility(View.GONE);
//            holder.textViewCCAChoice2.setText("Choice 2 : Nil");
//
//        } else {
//            holder.linearChoice2.setVisibility(View.VISIBLE);
//            holder.textViewCCAChoice2.setText( mCCADetailModelArrayList.get(position).getChoice2());
//
//            if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice2.setText("("+ convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+" - "+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
//
//            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time())+")");
//            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_time()!=null)
//            {
//                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
//
//                holder.textViewCCAaDateItemChoice2.setText("("+convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time())+")");
//            }
//            else
//            {
//                holder.textViewCCAaDateItemChoice2.setVisibility(View.GONE);
//
//            }
//
//        }
//        if (((mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) && ((mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
//            holder.attendanceListIcon.setVisibility(View.INVISIBLE);
//            holder.deleteChoice1.setVisibility(View.INVISIBLE);
//            holder.deleteChoice2.setVisibility(View.INVISIBLE);
//        } else {
//            holder.attendanceListIcon.setVisibility(View.INVISIBLE);
//            holder.deleteChoice1.setVisibility(View.INVISIBLE);
//            holder.deleteChoice2.setVisibility(View.INVISIBLE);
//
//        }


    }


    @Override
    public int getItemCount() {

        return mCCADetailModelArrayList.size();
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


    public void showAttendanceList(int mPosition) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        LinearLayout linearChoice3 = (LinearLayout) dialog.findViewById(R.id.linearChoice1);
        LinearLayout linearChoice4 = (LinearLayout) dialog.findViewById(R.id.linearChoice2);
        TextView alertHead = (TextView) dialog.findViewById(R.id.alertHead);
        TextView textViewCCAChoiceFirst = (TextView) dialog.findViewById(R.id.textViewCCAChoice1);
        TextView textViewCCAChoiceSecond = (TextView) dialog.findViewById(R.id.textViewCCAChoice2);
        ScrollView scrollViewMain = (ScrollView) dialog.findViewById(R.id.scrollViewMain);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        RecyclerView recycler_view_social_mediaChoice2 = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_mediaChoice2);
        alertHead.setText("Attendance report of " + mCCADetailModelArrayList.get(mPosition).getDay());

        scrollViewMain.smoothScrollTo(0,0);
        if (!(mCCADetailModelArrayList.get(mPosition).getChoice1().equalsIgnoreCase("0")) && !(mCCADetailModelArrayList.get(mPosition).getChoice1().equalsIgnoreCase("-1"))) {
            textViewCCAChoiceFirst.setText(mCCADetailModelArrayList.get(mPosition).getChoice1());
            linearChoice3.setVisibility(View.VISIBLE);
            socialMediaList.setVisibility(View.VISIBLE);
            socialMediaList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            socialMediaList.setLayoutManager(llm);
            CCAAttendenceListAdapter socialMediaAdapter = new CCAAttendenceListAdapter(mContext, mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice1());
            socialMediaList.setAdapter(socialMediaAdapter);
        } else {
            linearChoice3.setVisibility(View.GONE);
            socialMediaList.setVisibility(View.GONE);

        }


        if (!(mCCADetailModelArrayList.get(mPosition).getChoice2().equalsIgnoreCase("0")) && !(mCCADetailModelArrayList.get(mPosition).getChoice2().equalsIgnoreCase("-1"))) {
            textViewCCAChoiceSecond.setText(mCCADetailModelArrayList.get(mPosition).getChoice2());

            linearChoice4.setVisibility(View.VISIBLE);
            recycler_view_social_mediaChoice2.setVisibility(View.VISIBLE);
            recycler_view_social_mediaChoice2.setHasFixedSize(true);
            LinearLayoutManager llmrecycler_view_social_mediaChoice2 = new LinearLayoutManager(mContext);
            llmrecycler_view_social_mediaChoice2.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_view_social_mediaChoice2.setLayoutManager(llmrecycler_view_social_mediaChoice2);
            CCAAttendenceListAdapter socialMediaAdapterChoice2 = new CCAAttendenceListAdapter(mContext, mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice2());
            recycler_view_social_mediaChoice2.setAdapter(socialMediaAdapterChoice2);
        } else {
            linearChoice4.setVisibility(View.GONE);
            recycler_view_social_mediaChoice2.setVisibility(View.GONE);

        }
        dialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
