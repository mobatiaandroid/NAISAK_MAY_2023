package com.nas.naisak.activity.cca.model;

import java.io.Serializable;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class WeekListModel implements Serializable {
  String weekDay;
  String weekDayMMM;
  String choiceStatus;
  String choiceStatus1;
  String dataInWeek;

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
 public String getWeekDayMMM() {
        return weekDayMMM;
    }

    public void setWeekDayMMM(String weekDayMMM) {
        this.weekDayMMM = weekDayMMM;
    }

    public String getChoiceStatus() {
        return choiceStatus;
    }

    public void setChoiceStatus(String choiceStatus) {
        this.choiceStatus = choiceStatus;
    }   public String getChoiceStatus1() {
        return choiceStatus1;
    }

    public void setChoiceStatus1(String choiceStatus) {
        this.choiceStatus1 = choiceStatus;
    }

    public String getDataInWeek() {
        return dataInWeek;
    }

    public void setDataInWeek(String dataInWeek) {
        this.dataInWeek = dataInWeek;
    }
}
