package com.nas.naisak.activity.cca.model;

import java.io.Serializable;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class StudentModel implements Serializable{

    String mId;

    public String getGoingStatus() {
        return goingStatus;
    }

    public void setGoingStatus(String goingStatus) {
        this.goingStatus = goingStatus;
    }

    String goingStatus;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }




    String mName;String mClass;
    String mSection;

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmHouse() {
        return mHouse;
    }

    public void setmHouse(String mHouse) {
        this.mHouse = mHouse;
    }

    public String getmSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    String mPhoto;
    String mHouse;


}
