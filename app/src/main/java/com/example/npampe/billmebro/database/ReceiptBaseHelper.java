package com.example.npampe.billmebro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

public class ReceiptBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "receipt_list.db";

    private static final String TAG = "ReceiptBaseHelper";

    public ReceiptBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating new receipt database");
        db.execSQL("create table " + ReceiptTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        ReceiptTable.Cols.UUID + ", " +
                        ReceiptTable.Cols.TITLE + ", " +
                        ReceiptTable.Cols.DATE + ", " +
                        ReceiptTable.Cols.DAY_OF_YEAR + ", " +
                        ReceiptTable.Cols.TOTAL +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing
    }

}
