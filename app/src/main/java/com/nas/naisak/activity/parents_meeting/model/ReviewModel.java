package com.nas.naisak.activity.parents_meeting.model;

import java.io.Serializable;

/**
 * Created by gayatri on 17/3/17.
 */
public class ReviewModel implements Serializable{
    String studentId;
    String id;
    String staffId;
    String studentName;
    String studentPhoto;
    String startTimeFormated;
    String book_end_date;
    String booking_open;
    String vpml;
    String room;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getVpml() {
        return vpml;
    }

    public void setVpml(String vpml) {
        this.vpml = vpml;
    }

    public String getStartTimeFormated() {
        return startTimeFormated;
    }

    public void setStartTimeFormated(String startTimeFormated) {
        this.startTimeFormated = startTimeFormated;
    }

    public String getEndTimeFormated() {
        return endTimeFormated;
    }

    public void setEndTimeFormated(String endTimeFormated) {
        this.endTimeFormated = endTimeFormated;
    }

    String endTimeFormated;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getDateAppointment() {
        return dateAppointment;
    }

    public void setDateAppointment(String dateAppointment) {
        this.dateAppointment = dateAppointment;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    String staffName;
    String dateAppointment;
    String startTime;
    String endTime;
    String className;
    String status;//3-confirmed,2-pending

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_end_date() {
        return book_end_date;
    }

    public void setBook_end_date(String book_end_date) {
        this.book_end_date = book_end_date;
    }

    public String getBooking_open() {
        return booking_open;
    }

    public void setBooking_open(String booking_open) {
        this.booking_open = booking_open;
    }
}
