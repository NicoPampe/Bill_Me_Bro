package com.example.npampe.billmebro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

/**
 * Created by Nick Pampe on 4/9/2016.
 */
public class ReceiptBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABAE_NAME = "receiptBase.db";

    public ReceiptBaseHelper(Context context) {
        super(context, DATABAE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ReceiptTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ReceiptTable.Cols.UUID + ", " +
                ReceiptTable.Cols.TITLE + ", " +
                ReceiptTable.Cols.DATE + ", " +
                ReceiptTable.Cols.TOTOAL + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
