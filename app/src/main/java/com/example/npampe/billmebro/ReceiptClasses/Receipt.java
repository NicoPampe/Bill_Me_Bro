package com.example.npampe.billmebro.ReceiptClasses;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Receipt
        implements Serializable {

    private static final String TAG = "Receipt";

    private ArrayList<DateFormat> mDateFormats;

    private UUID mId = UUID.randomUUID();

    public UUID getGroupId() {
        return mGroupId;
    }

    public void setGroupId(UUID groupId) {
        mGroupId = groupId;
    }

    private UUID mGroupId;
    private String mTitle;
    private Date mDate;
    private int mDayOfYear;
    private double mTotal;

    // TODO: Initialize list of conflicts

    public Receipt() {
        this(UUID.randomUUID());
        mDateFormats = new ArrayList<DateFormat>();
        mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
        mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public Receipt(String title) {
        mTitle = title;
        mDateFormats = new ArrayList<DateFormat>();
        mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
        mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
        mTotal = 0;

        Calendar calendar = Calendar.getInstance();
        mDate = calendar.getTime();
        mDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    }

    public Receipt(UUID uuid) {
        mId = uuid;
        mTotal = 0;
        mDate = new Date();
        Calendar calendar = Calendar.getInstance();
        mDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        mDateFormats = new ArrayList<DateFormat>();
        mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
        mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
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

    public String getFormattedDate() {
        return mDateFormats.get(0).format(mDate);
    }


    public void setDate(Date date) {
        mDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    }

    public UUID getId() {
        return mId;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public int getDayOfYear() {
        return mDayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        mDayOfYear = dayOfYear;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        mDate.setTime(calendar.getTimeInMillis());
    }
}
