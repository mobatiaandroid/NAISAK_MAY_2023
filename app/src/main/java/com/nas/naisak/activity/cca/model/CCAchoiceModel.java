package com.nas.naisak.activity.cca.model;

import java.io.Serializable;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class CCAchoiceModel implements Serializable {

    String cca_details_id;
    String cca_item_name;
    String status;
    String dayChoice;
    String choice2Empty;
    String choice1Empty;
    String cca_item_start_time;
    String cca_item_end_time;
    Boolean disableCccaiem;
    String venue;
    String venue2;
    String description;
    String description2;

    public String getVenue2() {
        return venue2;
    }

    public void setVenue2(String venue2) {
        this.venue2 = venue2;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsattending() {
        return isattending;
    }

    public void setIsattending(String isattending) {
        this.isattending = isattending;
    }

    String isattending;

    public String getCca_details_id() {
        return cca_details_id;
    }

    public void setCca_details_id(String cca_details_id) {
        this.cca_details_id = cca_details_id;
    }

    public String getCca_item_name() {
        return cca_item_name;
    }

    public void setCca_item_name(String cca_item_name) {
        this.cca_item_name = cca_item_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayChoice() {
        return dayChoice;
    }

    public void setDayChoice(String dayChoice) {
        this.dayChoice = dayChoice;
    }

    public String getChoice2Empty() {
        return choice2Empty;
    }

    public void setChoice2Empty(String choice2Empty) {
        this.choice2Empty = choice2Empty;
    }    public String getChoice1Empty() {
        return choice1Empty;
    }

    public void setChoice1Empty(String choice1Empty) {
        this.choice1Empty = choice1Empty;
    }

    public String getCca_item_start_time() {
        return cca_item_start_time;
    }

    public void setCca_item_start_time(String cca_item_start_time) {
        this.cca_item_start_time = cca_item_start_time;
    }

    public String getCca_item_end_time() {
        return cca_item_end_time;
    }

    public void setCca_item_end_time(String cca_item_end_time) {
        this.cca_item_end_time = cca_item_end_time;
    }

    public Boolean getDisableCccaiem() {
        return disableCccaiem;
    }

    public void setDisableCccaiem(Boolean disableCccaiem) {
        this.disableCccaiem = disableCccaiem;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
