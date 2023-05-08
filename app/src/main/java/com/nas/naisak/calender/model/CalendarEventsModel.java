package com.nas.naisak.calender.model;

import java.io.Serializable;

public class CalendarEventsModel implements Serializable {
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	String eventdate;
	String status;
	String startTime;
	String booking_open;
	String vpml;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int id;

	public String getVpml() {
		return vpml;
	}

	public void setVpml(String vpml) {
		this.vpml = vpml;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	String endTime;

	public String getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	String eventStartDate;
	String eventEndDate;
	String book_end_date;
	String room;

	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	int year,month,day;

	public String getMonthString() {
		return monthString;
	}

	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}

	String monthString;

	public String geteventdate() {
		return eventdate;
	}

	public void seteventdate(String eventdate) {
		this.eventdate = eventdate;
	}



	public void setStatus(String status) {
		// TODO Auto-generated method stub
		this.status=status;
	}
	public String getStatus() {
		return status;
	}

	public String getBook_end_date() {
		return book_end_date;
	}

	public void setBook_end_date(String book_end_date) {
		this.book_end_date = book_end_date;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBooking_open() {
		return booking_open;
	}

	public void setBooking_open(String booking_open) {
		this.booking_open = booking_open;
	}
}
