package com.nas.naisak.activity.cca.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class CCAModel implements Serializable {
    String cca_days_id;
    String from_date;
    String to_date;
    String title;
    String submission_dateTime;
    String isAttendee;
    String isSubmissionDateOver;
    ArrayList<CCADetailModel> details;

    public String getCca_days_id() {
        return cca_days_id;
    }

    public void setCca_days_id(String cca_days_id) {
        this.cca_days_id = cca_days_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getSubmission_dateTime() {
        return submission_dateTime;
    }

    public void setSubmission_dateTime(String submission_dateTime) {
        this.submission_dateTime = submission_dateTime;
    }

    public ArrayList<CCADetailModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<CCADetailModel> details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAttendee() {
        return isAttendee;
    }

    public void setIsAttendee(String isAttendee) {
        this.isAttendee = isAttendee;
    }

    public String getIsSubmissionDateOver() {
        return isSubmissionDateOver;
    }

    public void setIsSubmissionDateOver(String isSubmissionDateOver) {
        this.isSubmissionDateOver = isSubmissionDateOver;
    }
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
