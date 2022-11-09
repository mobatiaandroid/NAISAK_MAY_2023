package com.nas.naisak.calender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nas.naisak.R;
import com.nas.naisak.calender.model.CalendarEventsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExtendedCalendarView extends RelativeLayout implements
		OnItemClickListener, OnClickListener {
	ListView eventList;
	int monthFromCal, dayFromCal, yearFromCal;
	private Context context;
	private OnDayClickListener dayListener;
	private GridView calendar;
	private CalendarAdapter mAdapter;
	private Calendar cal;
	private TextView month;
	private RelativeLayout base;
	private ImageView next, prev;
	private int gestureType = 0;
	private ArrayList<String> days;
	private final GestureDetector calendarGesture = new GestureDetector(
			context, new GestureListener());

	public static final int NO_GESTURE = 0;
	public static final int LEFT_RIGHT_GESTURE = 1;
	public static final int UP_DOWN_GESTURE = 2;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private ArrayList<CalendarEventsModel> mCalendarEvents;
	private ArrayList<CalendarEventsModel> tempArrayList,
			tempArrayEventListNew;

	public interface OnDayClickListener {
		void onDayClicked(AdapterView<?> adapter, View view,
                          int position, long id, Day day);
	}

	public ExtendedCalendarView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ExtendedCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ExtendedCalendarView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	@SuppressWarnings("deprecation")
	private void init(ArrayList<CalendarEventsModel> calendarEvents) {
		// cal = Calendar.getInstance();
		// base = new RelativeLayout(context);
		// base.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// base.setMinimumHeight(50);
		//
		// base.setId(4);
		//
		// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// params.leftMargin = 16;
		// params.topMargin = 50;
		// params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		// params.addRule(RelativeLayout.CENTER_VERTICAL);
		// prev = new ImageView(context);
		// prev.setId(1);
		// prev.setLayoutParams(params);
		// prev.setImageResource(R.drawable.navigation_previous_item);
		// prev.setOnClickListener(this);
		// base.addView(prev);
		//
		// params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// params.addRule(RelativeLayout.CENTER_VERTICAL);
		// month = new TextView(context);
		// month.setId(2);
		// month.setLayoutParams(params);
		// month.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		// month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
		// Locale.getDefault())
		// + " " + cal.get(Calendar.YEAR));
		// month.setTextSize(25);
		// base.addView(month);
		//
		// params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// params.rightMargin = 16;
		// params.topMargin = 50;
		// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// params.addRule(RelativeLayout.CENTER_VERTICAL);
		// next = new ImageView(context);
		// next.setImageResource(R.drawable.navigation_next_item);
		// next.setLayoutParams(params);
		// next.setId(3);
		// next.setOnClickListener(this);
		//
		// base.addView(next);
		//
		// addView(base);
		//
		// params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// params.bottomMargin = 20;
		// params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		// params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// params.addRule(RelativeLayout.BELOW, base.getId());

		// calendar = new GridView(context);
		// calendar.setSelector(R.drawable.gridselect);
		// calendar.setLayoutParams(params);
		// calendar.setVerticalSpacing(4);
		// calendar.setHorizontalSpacing(4);
		// calendar.setNumColumns(7);
		// calendar.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		// calendar.setDrawSelectorOnTop(true);
		// tempArrayList = new ArrayList<CalendarEventsModel>();
		// if (mCalendarEvents != null) {
		// for (int i = 0; i < mCalendarEvents.size(); i++) {
		// String dateString = mCalendarEvents.get(i).geteventdate();
		// System.out.println("dateString|" + dateString + "|");
		// if (dateString != null) {
		//
		// SimpleDateFormat format = new SimpleDateFormat(
		// "yyyy-MM-dd HH:mm:ss");
		// try {
		// Date date = format.parse(dateString);
		// // System.out.println("date:"+date);
		// Calendar cal1 = Calendar.getInstance();
		// cal1.setTime(date);
		// System.out.println("date:|" + cal1.get(Calendar.YEAR)
		// + "|" + "Calender:|" + cal.get(Calendar.YEAR)
		// + "|");
		// if (cal1.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
		// && cal1.get(Calendar.MONTH) == cal
		// .get(Calendar.MONTH)) {
		//
		// tempArrayList.add(mCalendarEvents.get(i));
		//
		// }
		// } catch (java.text.ParseException e) {
		// e.getLocalizedMessage();
		// }
		// }
		// }
		// }
		// if (tempArrayList != null) {
		// for (int j = 0; j < tempArrayList.size(); j++) {
		// System.out.println("Date:"
		// + tempArrayList.get(j).geteventdate());
		// }
		// }

		// mAdapter = new CalendarAdapter(context, cal);
		// calendar.setAdapter(mAdapter);
		// calendar.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// return calendarGesture.onTouchEvent(event);
		// }
		// });
		//
		// addView(calendar);
		refreshCalendar();
	}

	void setArrayListToSelectedMonth() {
		tempArrayList = new ArrayList<CalendarEventsModel>();

		if (mCalendarEvents != null) {
			for (int i = 0; i < mCalendarEvents.size(); i++) {
				String dateString = mCalendarEvents.get(i).geteventdate();
				System.out.println("dateString|" + dateString + "|");
				if (dateString != null) {

					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd", Locale.ENGLISH);
					try {
						Date date = format.parse(dateString);
						// System.out.println("date:"+date);
						Calendar cal1 = Calendar.getInstance();
						cal1.setTime(date);
						System.out.println("date:|" + cal1.get(Calendar.YEAR)
								+ "|" + "Calender:|" + cal.get(Calendar.YEAR)
								+ "|");
						System.out.println("dateM:|" + cal1.get(Calendar.MONTH)
								+ "|" + "CalenderM:|" + cal.get(Calendar.MONTH)
								+ "|");
						//
						// 05-24 16:12:53.004: I/System.out(26813):
						// dateString|2016-05-31 13:20:00|
						// 05-24 16:12:53.005: I/System.out(26813):
						// date:|2016|Calender:|2016|
						// 05-24 16:12:53.005: I/System.out(26813):
						// dateString|2016-05-31 14:20:00|
						// 05-24 16:12:53.006: I/System.out(26813):
						// date:|2016|Calender:|2016|
						// 05-24 16:12:53.006: I/System.out(26813):
						// dateString|2016-06-15 14:21:00|
						// 05-24 16:12:53.006: I/System.out(26813):
						// date:|2016|Calender:|2016|

						if (cal1.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
								&& cal1.get(Calendar.MONTH) == cal
										.get(Calendar.MONTH)) {

							tempArrayList.add(mCalendarEvents.get(i));

						}
					} catch (java.text.ParseException e) {
						e.getLocalizedMessage();
					}
				}
			}
			for (int i = 0; i < tempArrayList.size(); i++) {
				System.out.println(tempArrayList.get(i).geteventdate());
			}
			days = new ArrayList<String>();
			if (tempArrayList.size() > 0) {

				ArrayList<CalendarEventsModel> asds = new ArrayList<CalendarEventsModel>();
				asds.add(tempArrayList.get(0));

				days.add(String.valueOf(tempArrayList.get(0).getDay()));
				int date123 = tempArrayList.get(0).getDay();
				for (int i = 1; i < tempArrayList.size(); i++) {
					System.out.println("Date123k"+date123+"k5555k"+tempArrayList.get(i)
							.geteventdate()+"k");
					if (date123 != tempArrayList.get(i)
							.getDay()) {
						asds.add(tempArrayList.get(i));
						days.add(String.valueOf(tempArrayList.get(i).getDay()));
						date123 = tempArrayList.get(i).getDay();
					}
				}
//				tempArrayList = new ArrayList<CalendarEventsModel>();
//				tempArrayList = asds;
			}
			/*
			 * CalendarEventListAdapter calendarEventListAdapter = new
			 * CalendarEventListAdapter( context, tempArrayList);
			 * setTempArrayList(tempArrayList);
			 * eventList.setAdapter(calendarEventListAdapter);
			 */
		}
	}

	// public static Calendar DateToCalendar(Date date){
	// Calendar cal1 = Calendar.getInstance();
	// cal1.setTime(date);
	// return cal1;
	// }
	private void init() {
		cal = Calendar.getInstance();
		base = new RelativeLayout(context);
		base.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		base.setMinimumHeight(40);
		base.setBackgroundResource(R.color.split_bg);
		base.setId(Integer.valueOf("4"));

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.leftMargin = 16;
		params.topMargin = 10;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		prev = new ImageView(context);
		prev.setId(Integer.valueOf("1"));
		prev.setLayoutParams(params);
		prev.setImageResource(R.drawable.navigation_previous_item);
		prev.setOnClickListener(this);
		base.addView(prev);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		month = new TextView(context);
		month.setId(Integer.valueOf("2"));
		month.setLayoutParams(params);
//		month.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault())
				+ " " + cal.get(Calendar.YEAR));
		month.setTextSize(20);
		month.setTextColor(Color.parseColor("#FFFFFF"));
		base.addView(month);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.rightMargin = 16;
//		params.topMargin = 50;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		next = new ImageView(context);
		next.setImageResource(R.drawable.navigation_next_item);
		next.setLayoutParams(params);
		next.setId(Integer.valueOf("3"));
		next.setOnClickListener(this);
		base.addView(next);

		addView(base);


		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 10;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.BELOW, base.getId());

		calendar = new GridView(context);
		calendar.setSelector(R.drawable.gridselect);
		calendar.setLayoutParams(params);
		calendar.setVerticalSpacing(1);
		calendar.setHorizontalSpacing(1);
		calendar.setNumColumns(7);
//		calendar.setBackgroundResource(R.color.green);

		calendar.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		calendar.setDrawSelectorOnTop(true);
		calendar.setVerticalScrollBarEnabled(true);
        calendar.setScrollBarFadeDuration(0);
		setMonthPrevNextVisibility(1);
		mAdapter = new CalendarAdapter(context, cal);
		calendar.setAdapter(mAdapter);
		calendar.setOnTouchListener((v, event) -> calendarGesture.onTouchEvent(event));

		addView(calendar);
	}

	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			if (gestureType == LEFT_RIGHT_GESTURE) {
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					nextMonth();
					return true; // Right to left
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					previousMonth();
					return true; // Left to right
				}
			} else if (gestureType == UP_DOWN_GESTURE) {
				if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					nextMonth();
					return true; // Bottom to top
				} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					previousMonth();
					return true; // Top to bottom
				}
			}
			return false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (dayListener != null) {

			Day d = (Day) mAdapter.getItem(arg2);
			if (d.getDay() != 0) {
				dayListener.onDayClicked(arg0, arg1, arg2, arg3, d);
			}
		}
	}

	/**
	 *
	 * @param listener
	 *
	 *            Set a listener for when you press on a day in the month
	 */
	public void setOnDayClickListener(OnDayClickListener listener) {
		if (calendar != null) {
			dayListener = listener;
			calendar.setOnItemClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 1:
			previousMonth();
			break;
		case 3:
			nextMonth();
			break;
		default:
			break;
		}
	}

	private void previousMonth() {
		if (cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) {
			cal.set((cal.get(Calendar.YEAR) - 1),
					cal.getActualMaximum(Calendar.MONTH), 1);
		} else {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		}
		rebuildCalendar();
	}

	private void nextMonth() {
		if (cal.get(Calendar.MONTH) == cal.getActualMaximum(Calendar.MONTH)) {
			cal.set((cal.get(Calendar.YEAR) + 1),
					cal.getActualMinimum(Calendar.MONTH), 1);
		} else {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		}
		rebuildCalendar();
	}

	private void rebuildCalendar() {
		if (month != null) {
			month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
					Locale.getDefault()) + " " + cal.get(Calendar.YEAR));

			setMonthFromCal(cal.get(Calendar.MONTH));
			setyearFromCal(cal.get(Calendar.YEAR));
			setdayFromCal(cal.get(Calendar.DAY_OF_MONTH));
			refreshCalendar();
		}
	}

	/**
	 * Refreshes the month
	 */
	public void refreshCalendar() {
//		if (eventList.getVisibility() == View.VISIBLE) {
//			eventList.setVisibility(View.INVISIBLE);
//		}
		mAdapter.refreshDays();
		mAdapter.notifyDataSetChanged();
		setArrayListToSelectedMonth();
		mAdapter = new CalendarAdapter(context, cal, tempArrayList, days);
		calendar.setAdapter(mAdapter);
	}

	/**
	 *
	 * @param color
	 *
	 *            Sets the background color of the month bar
	 */
	public void setMonthTextBackgroundColor(int color) {
		base.setBackgroundColor(color);
	}

	@SuppressLint("NewApi")
	/**
	 *
	 * @param drawable
	 *
	 * Sets the background color of the month bar. Requires at least API level 16
	 */
	public void setMonthTextBackgroundDrawable(Drawable drawable) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			base.setBackground(drawable);
		}

	}

	/**
	 *
	 * @param resource
	 *
	 *            Sets the background color of the month bar
	 */
	public void setMonehtTextBackgroundResource(int resource) {
		base.setBackgroundResource(resource);
	}

	/**
	 *
	 * @param recource
	 *
	 *            change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageResource(int recource) {

		prev.setImageResource(recource);
	}

	/**
	 *
	 * @param bitmap
	 *
	 *            change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageBitmap(Bitmap bitmap) {
		prev.setImageBitmap(bitmap);
	}

	/**
	 *
	 * @param drawable
	 *
	 *            change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageDrawable(Drawable drawable) {
		prev.setImageDrawable(drawable);
	}

	/**
	 *
	 * @param recource
	 *
	 *            change the image of the next month button
	 */
	public void setNextMonthButtonImageResource(int recource) {

		next.setImageResource(recource);
	}

	/**
	 *
	 * @param bitmap
	 *
	 *            change the image of the next month button
	 */
	public void setNextMonthButtonImageBitmap(Bitmap bitmap) {

		next.setImageBitmap(bitmap);
	}

	/**
	 *
	 * @param drawable
	 *
	 *            change the image of the next month button
	 */
	public void setNextMonthButtonImageDrawable(Drawable drawable) {
		next.setImageDrawable(drawable);
	}

	/**
	 *
	 * @param gestureType
	 *
	 *            Allow swiping the calendar left/right or up/down to change the
	 *            month.
	 *
	 *            Default value no gesture
	 */
	public void setGesture(int gestureType) {
		this.gestureType = gestureType;
	}

	public void setMonthFromCal(int month) {
		this.monthFromCal = month;
	}

	public String getMonthFromCal() {
		return (month.getText().toString());
	}

	public void setdayFromCal(int day) {
		this.dayFromCal = day;
	}

	public int getdayFromCal() {
		return (dayFromCal);
	}

	public void setyearFromCal(int year) {
		this.yearFromCal = year;
	}

	public int getyearFromCal() {
		return (yearFromCal);
	}

	public void setMonthPrevNextVisibility(int visibilty) {
		if (visibilty == 0) {
			month.setVisibility(View.GONE);
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
		}
		else if (visibilty == 1) {
			month.setVisibility(View.VISIBLE);
			prev.setVisibility(View.INVISIBLE);
			next.setVisibility(View.INVISIBLE);
		}
		else {
			month.setVisibility(View.VISIBLE);
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
		}

	}

	public void datesForDays(ArrayList<CalendarEventsModel> calendarEvents,
			ListView eventsList) {
		this.mCalendarEvents = calendarEvents;
		this.eventList = eventsList;
		init(calendarEvents);

	}
	public void datesForDays(ArrayList<CalendarEventsModel> calendarEvents) {
		this.mCalendarEvents = calendarEvents;
		init(calendarEvents);

	}

	public void setTempArrayList(ArrayList<CalendarEventsModel> tempArrayListNew) {
		this.tempArrayEventListNew = new ArrayList<CalendarEventsModel>();
		this.tempArrayEventListNew = tempArrayListNew;
	}

	public ArrayList<CalendarEventsModel> getTempArrayList() {
		return (tempArrayEventListNew);
	}

	public Calendar getCalendar() {
		return (cal);
	}
	public String getMonthOfYear(int month) {
		String monthOfYear = "";
		switch (month) {
		case 0:
			monthOfYear = context.getResources().getString(R.string.january);
			break;
		case 1:
			monthOfYear = context.getResources().getString(R.string.february);
			break;
		case 2:
			monthOfYear = context.getResources().getString(R.string.march);
			break;
		case 3:
			monthOfYear = context.getResources().getString(R.string.april);
			break;
		case 4:
			monthOfYear = context.getResources().getString(R.string.may);
			break;
		case 5:
			monthOfYear = context.getResources().getString(R.string.june);
			break;
		case 6:
			monthOfYear = context.getResources().getString(R.string.july);
			break;
		case 7:
			monthOfYear = context.getResources().getString(R.string.august);
			break;
		case 8:
			monthOfYear = context.getResources().getString(R.string.september);
			break;
		case 9:
			monthOfYear = context.getResources().getString(R.string.october);
			break;
		case 10:
			monthOfYear = context.getResources().getString(R.string.november);
			break;
		case 11:
			monthOfYear = context.getResources().getString(R.string.december);
			break;
		default:
			break;
		}
		return monthOfYear;

	}
}
