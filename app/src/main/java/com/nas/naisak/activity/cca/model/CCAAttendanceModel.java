package com.nas.naisak.activity.cca.model;

import java.io.Serializable;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class CCAAttendanceModel implements Serializable {

String dateAttend;
String statusCCA;

    public String getDateAttend() {
        return dateAttend;
    }

    public void setDateAttend(String dateAttend) {
        this.dateAttend = dateAttend;
    }

    public String getStatusCCA() {
        return statusCCA;
    }

    public void setStatusCCA(String statusCCA) {
        this.statusCCA = statusCCA;
    }
}
