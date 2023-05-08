package com.nas.naisak.calender;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nas.naisak.R;
import com.nas.naisak.calender.model.CalendarEventsModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalendarAdapter extends BaseAdapter {

	static final int FIRST_DAY_OF_WEEK = 0;
	Context context;
	Calendar cal;
	public String[] days;
	public ArrayList<CalendarEventsModel> mCalenderEventsArrayList;
	private int indexValue = 0;
	private String dateString = "";
	private final ArrayList<String> daysArralist;

	// OnAddNewEventClick mAddEvent;

	ArrayList<Day> dayList = new ArrayList<Day>();

	public CalendarAdapter(Context context, Calendar cal,
			ArrayList<CalendarEventsModel> calenderEventsArrayList, ArrayList<String> days) {
		this.cal = cal;
		this.context = context;
		cal.set(Calendar.DAY_OF_MONTH, 1);
		this.mCalenderEventsArrayList = calenderEventsArrayList;
		refreshDays();
		this.daysArralist = days;
		if (calenderEventsArrayList.size() > 0) {
			dateString = "" + mCalenderEventsArrayList.get(0).getYear()
					+ mCalenderEventsArrayList.get(0).getMonth()
					+ mCalenderEventsArrayList.get(0).getDay();
		}
//		System.out.println("mCalenderEventsArrayList count"
//				+ mCalenderEventsArrayList.size());
		indexValue = 0;
		// if (mCalenderEventsArrayList.size() > 0) {
		//
		// ArrayList<CalendarEventsModel> asds = new
		// ArrayList<CalendarEventsModel>();
		// asds.add(mCalenderEventsArrayList.get(0));
		// int date123 = mCalenderEventsArrayList.get(0).getDay();
		// for (int i = 1; i < mCalenderEventsArrayList.size(); i++) {
		// System.out.println("Date123k"+date123+"k5555k"+mCalenderEventsArrayList.get(i)
		// .geteventdate()+"k");
		// if (date123 != mCalenderEventsArrayList.get(i)
		// .getDay()) {
		// asds.add(mCalenderEventsArrayList.get(i));
		// date123 = mCalenderEventsArrayList.get(i).getDay();
		// }
		// }
		// mCalenderEventsArrayList = new ArrayList<CalendarEventsModel>();
		// mCalenderEventsArrayList = asds;
		// }
		// refreshDays();
	}

	public CalendarAdapter(Context context, Calendar cal) {
		this.cal = cal;
		this.context = context;
		cal.set(Calendar.DAY_OF_MONTH, 1);
		mCalenderEventsArrayList = new ArrayList<CalendarEventsModel>();
		daysArralist = new ArrayList<String>();
		refreshDays();
	}

	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int position) {
		return dayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public int getPrevMonth() {
		if (cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) {
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR - 1));
		} else {

		}
		int month = cal.get(Calendar.MONTH);
		if (month == 0) {
			return month = 11;
		}

		return month - 1;
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (position >= 0 && position < 7) {
			v = vi.inflate(R.layout.layout_day_of_week, null);
			TextView day = v.findViewById(R.id.textView1);
			day.setTextColor(Color.parseColor("#000000"));//8423E5
			if (position == 0) {
				day.setText(R.string.sunday);
			} else if (position == 1) {
				day.setText(R.string.monday);
			} else if (position == 2) {
				day.setText(R.string.tuesday);
			} else if (position == 3) {
				day.setText(R.string.wednesday);
			} else if (position == 4) {
				day.setText(R.string.thursday);
			} else if (position == 5) {
				day.setText(R.string.friday);
			} else if (position == 6) {
				day.setText(R.string.saturday);
			}

		} else {

			v = vi.inflate(R.layout.layout_day_view, null);
			FrameLayout today = v.findViewById(R.id.today_frame);
			FrameLayout notifyDay = v
					.findViewById(R.id.notify_frame);

			TextView dayTV = v.findViewById(R.id.textView1);
			Calendar cal = Calendar.getInstance(TimeZone.getDefault(),
					Locale.getDefault());
			Day d = dayList.get(position);
			// System.out.println("Year:" + d.getYear() + "cal"
			// + cal.get(Calendar.YEAR));
			// if (mCalenderEventsArrayList.get(indexValue).getYear() == )

			if (d.getYear() == cal.get(Calendar.YEAR)
					&& d.getMonth() == cal.get(Calendar.MONTH)
					&& d.getDay() == cal.get(Calendar.DAY_OF_MONTH)) {
				today.setVisibility(View.VISIBLE);
				dayTV.setTextColor(Color.parseColor("#000000"));//FFFFFF
			} else {
				today.setVisibility(View.GONE);
				dayTV.setTextColor(Color.parseColor("#000000"));
			}
//			System.out.println("day" + d.getDay());
//			System.out.println("day" + mCalenderEventsArrayList.size());
//			
//			System.out.println("IndexValue"+indexValue);
////			if(indexValue+1 == mCalenderEventsArrayList.size()){
////				indexValue =0;
////			}
//			if(d.getDay()==1){
//				indexValue =0;
//			}
			if (daysArralist.size()>0) {
				if (daysArralist.contains(String.valueOf(d.getDay()))) {
					 notifyDay.setVisibility(View.VISIBLE);
					 dayTV.setTextColor(Color.parseColor("#FFFFFF"));
				}
			}
//			if (mCalenderEventsArrayList.size() > 0
//					&& indexValue < mCalenderEventsArrayList.size()) {
//				String tempDate = "" + d.getYear() + d.getMonth() + d.getDay();
//				if (d.getYear() == mCalenderEventsArrayList.get(indexValue)
//						.getYear()
//						&& d.getMonth() == mCalenderEventsArrayList.get(
//								indexValue).getMonth()
//						&& d.getDay() == mCalenderEventsArrayList.get(
//								indexValue).getDay()) {
//					 notifyDay.setVisibility(View.VISIBLE);
//					 dayTV.setTextColor(Color.parseColor("#FFFFFF"));
//					for(int i=indexValue; i< mCalenderEventsArrayList.size();i++){
//						  System.out.println("dateStringArray:" + "" + mCalenderEventsArrayList.get(i).getYear() + mCalenderEventsArrayList.get(i).getMonth() + mCalenderEventsArrayList.get(i).getDay() + "tempDate"
//						  + tempDate);
//						if (!tempDate.equalsIgnoreCase("" + mCalenderEventsArrayList.get(i).getYear() + mCalenderEventsArrayList.get(i).getMonth() + mCalenderEventsArrayList.get(i).getDay())){
//							indexValue = i;
//							break;
//						}
//					}
//				}
//			}
			

			// if (mCalenderEventsArrayList.size() > 0 && indexValue <
			// mCalenderEventsArrayList.size())
			// {
			// /*
			// * for(int i=0;i<mCalenderEventsArrayList.size();i++) {
			// * System.out.println("Working:"+i);
			// */
			// if (d.getYear() == mCalenderEventsArrayList.get(indexValue)
			// .getYear()
			// && d.getMonth() == mCalenderEventsArrayList.get(
			// indexValue).getMonth()
			// && d.getDay() == mCalenderEventsArrayList.get(
			// indexValue).getDay()) {
			// notifyDay.setVisibility(View.VISIBLE);
			// dayTV.setTextColor(Color.parseColor("#FFFFFF"));
			// System.out.println("index:"+indexValue);
			// // String tempDate = "" + d.getYear() + d.getMonth()
			// // + d.getDay();
			// // System.out.println("dateString:" + dateString + "tempDate"
			// // + tempDate);
			// indexValue++;
			//
			// // do {
			// // indexValue++;
			// // if (indexValue < mCalenderEventsArrayList.size()) {
			// // dateString = ""
			// // + mCalenderEventsArrayList.get(indexValue)
			// // .getYear()
			// // + mCalenderEventsArrayList.get(indexValue)
			// // .getMonth()
			// // + mCalenderEventsArrayList.get(indexValue)
			// // .getDay();
			// // System.out.println("dateString:"+dateString);
			// // }
			// // } while (dateString.equalsIgnoreCase(tempDate));
			//
			// // dateString = "" +
			// // mCalenderEventsArrayList.get(indexValue+1).getYear()
			// // + mCalenderEventsArrayList.get(indexValue+1).getMonth()
			// // + mCalenderEventsArrayList.get(indexValue+1).getDay();
			// // if (dateString.equalsIgnoreCase(tempDate)) {
			// // indexValue++;
			// //
			// // }
			// // if (!dateString.equalsIgnoreCase(tempDate)) {
			// // indexValue++;
			// // } else {
			// // dateString = tempDate;
			// // }
			//
			// // }
			// }
			// }

			RelativeLayout rl = v.findViewById(R.id.rl);
			ImageView iv = v.findViewById(R.id.imageView1);
			ImageView blue = v.findViewById(R.id.imageView2);
			ImageView purple = v.findViewById(R.id.imageView3);
			ImageView green = v.findViewById(R.id.imageView4);
			ImageView orange = v.findViewById(R.id.imageView5);
			ImageView red = v.findViewById(R.id.imageView6);

			blue.setVisibility(View.VISIBLE);
			purple.setVisibility(View.VISIBLE);
			green.setVisibility(View.VISIBLE);
			purple.setVisibility(View.VISIBLE);
			orange.setVisibility(View.VISIBLE);
			red.setVisibility(View.VISIBLE);

			iv.setVisibility(View.VISIBLE);
			dayTV.setVisibility(View.VISIBLE);
			rl.setVisibility(View.VISIBLE);

			Day day = dayList.get(position);

			if (day.getNumOfEvenets() > 0) {
				Set<Integer> colors = day.getColors();

				iv.setVisibility(View.INVISIBLE);
				blue.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				green.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				orange.setVisibility(View.INVISIBLE);
				red.setVisibility(View.INVISIBLE);

				if (colors.contains(0)) {
					iv.setVisibility(View.VISIBLE);
				}
				if (colors.contains(2)) {
					blue.setVisibility(View.VISIBLE);
				}
				if (colors.contains(4)) {
					purple.setVisibility(View.VISIBLE);
				}
				if (colors.contains(5)) {
					green.setVisibility(View.VISIBLE);
				}
				if (colors.contains(3)) {
					orange.setVisibility(View.VISIBLE);
				}
				if (colors.contains(1)) {
					red.setVisibility(View.VISIBLE);
				}

			} else {
				iv.setVisibility(View.INVISIBLE);
				blue.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				green.setVisibility(View.INVISIBLE);
				purple.setVisibility(View.INVISIBLE);
				orange.setVisibility(View.INVISIBLE);
				red.setVisibility(View.INVISIBLE);
			}

			if (day.getDay() == 0) {
				rl.setVisibility(View.GONE);
			} else {
				dayTV.setVisibility(View.VISIBLE);
				dayTV.setText(String.valueOf(day.getDay()));
			}
		}

		return v;
	}

	public void refreshDays() {
		// clear items
		dayList.clear();

		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + 7;
		int firstDay = cal.get(Calendar.DAY_OF_WEEK);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		TimeZone tz = TimeZone.getDefault();

		// figure size of the array
		if (firstDay == 1) {
			days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
		} else {
			days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
		}

		int j = FIRST_DAY_OF_WEEK;

		// populate empty days before first real day
		if (firstDay > 1) {
			for (j = 0; j < (firstDay - FIRST_DAY_OF_WEEK) + 7; j++) {
				days[j] = "";
				Day d = new Day(context, 0, 0, 0);
				dayList.add(d);
			}
		} else {
			for (j = 0; j < (FIRST_DAY_OF_WEEK * 6) + 7; j++) {
				days[j] = "";
				Day d = new Day(context, 0, 0, 0);
				dayList.add(d);
			}
			j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
		}

		// populate days
		int dayNumber = 1;

		if (j > 0 && dayList.size() > 0 && j != 1) {
			dayList.remove(j - 1);
		}

		for (int i = j - 1; i < days.length; i++) {
			Day d = new Day(context, dayNumber, year, month);

			Calendar cTemp = Calendar.getInstance();
			cTemp.set(year, month, dayNumber);
			int startDay = Time.getJulianDay(cTemp.getTimeInMillis(),
					TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp
							.getTimeInMillis())));

			d.setAdapter(this);
			d.setStartDay(startDay);

			days[i] = "" + dayNumber;
			dayNumber++;
			dayList.add(d);
		}
	}

	// public abstract static class OnAddNewEventClick{
	// public abstract void onAddNewEventClick();
	// }

}
