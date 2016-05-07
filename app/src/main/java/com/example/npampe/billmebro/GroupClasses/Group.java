package com.example.npampe.billmebro.GroupClasses;

import com.example.npampe.billmebro.ReceiptClasses.Receipt;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Group {
    private UUID mId;
    private String mName;
    private ArrayList<DateFormat> mDateFormats;
    private Date mDate;
    private ArrayList<Receipt> mReceipts;
    private String mType;
    private ArrayList<String> mMembers;

    public Group(String s) {
        mId = UUID.randomUUID();
        mName = s;
        mDateFormats = new ArrayList<DateFormat>();
        mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
        mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
        mMembers = new ArrayList<String>();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Receipt> getReceipts() {
        return mReceipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        mReceipts = receipts;
    }

    public String getDate(int i) {
        return mDateFormats.get(i).format(mDate);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public ArrayList<String> getMembers() {
        return mMembers;
    }

    public void setMembers(ArrayList<String> members) {
        mMembers = members;
    }

    public void clearMembers() {
        mMembers.clear();
    }

    public void addMember(String member) {
        mMembers.add(member);
    }
}
