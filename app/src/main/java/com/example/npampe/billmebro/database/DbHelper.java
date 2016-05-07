package com.example.npampe.billmebro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptDbSchema.GroupsListTable;
import com.example.npampe.billmebro.database.ReceiptDbSchema.GroupsTable;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptsTable;
import com.example.npampe.billmebro.database.ReceiptDbSchema.UsersTable;

public class DbHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "bill-me-bro.db";

    private static final String TAG = "DbHelper";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating new database");

        // Associate Users with their Username.
        $(db, "create table " + UsersTable.NAME + "(" +
                        "_id integer primary key, " +
                        UsersTable.Cols.USER_ID + ", " +
                        UsersTable.Cols.USERNAME + ")"
        );

        // Associate Group IDs with Group metadata.
        $(db, "create table " + GroupsTable.NAME + "(" +
                        "_id integer primary key, " +
                        GroupsTable.Cols.GROUP_ID + ", " +
                        GroupsTable.Cols.NAME + ", " +
                        GroupsTable.Cols.TYPE + "," +
                        GroupsTable.Cols.DATE + ")"
        );

        // Associate Users and their groups.
        $(db, "create table " + GroupsListTable.NAME + "(" +
                        GroupsListTable.Cols.GROUP_ID + ", " +
                        GroupsListTable.Cols.USER_ID + ")"
        );

        // Store receipts metadata & data.
        $(db, "create table " + ReceiptsTable.NAME + "(" +
                        "_id integer primary key, " +
                        ReceiptsTable.Cols.RECEIPT_ID + ", " +
                        ReceiptsTable.Cols.GROUP_ID + ", " +
                        ReceiptsTable.Cols.TITLE + ", " +
                        ReceiptsTable.Cols.DATE + ", " +
                        ReceiptsTable.Cols.DAY_OF_YEAR + ", " +
                        ReceiptsTable.Cols.TOTAL +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.wtf(TAG, "upUpgrade(" + oldVersion + ", " + newVersion + ")");
    }

    private void $(SQLiteDatabase db, String cmd) {
        Log.i(TAG, "Running SQL command: " + cmd);
        db.execSQL(cmd);
    }

}
