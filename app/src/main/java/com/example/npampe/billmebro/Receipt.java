package com.example.npampe.billmebro;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Receipt
        implements Serializable {
    private static final String TAG = "Receipt";

    private UUID mId = UUID.randomUUID();
    private String mTitle;
    private double mTotal;
    private Date mDate;
    private Calendar mCalendar;
    private String mPlace;
    private String mSummary;
    private String mNotes;

    // TODO: Initialize list of users
    // TODO: Initialize list of conflicts

    public Receipt() {
        this(UUID.randomUUID());
    }

    public Receipt(String title) {
        mTitle = title;

        Calendar calendar = Calendar.getInstance();
        mCalendar = calendar;
        mDate = calendar.getTime();
    }


    public Receipt(UUID uuid) {
        mId = uuid;
        mDate = new Date();
        mCalendar = Calendar.getInstance();
    }

    public String toString() {
        return "Receipt[" + mTitle + "; total=" + mTotal + "; date=" + mDate + "]";
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getTotal() {
        return mTotal;
    }

    public void setTotal(double total) {
        mTotal = total;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
        mCalendar.setTime(date);
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public UUID getId() {
        return mId;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }
}
