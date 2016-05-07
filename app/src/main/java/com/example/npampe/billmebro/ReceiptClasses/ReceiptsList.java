package com.example.npampe.billmebro.ReceiptClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.npampe.billmebro.database.DbHelper;
import com.example.npampe.billmebro.database.ReceiptCursorWrapper;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptsTable;
import com.example.npampe.billmebro.database.ReceiptDbSchema.UsersTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contains methods to manipulate the database of receipt, group, and user data.
 */
public class ReceiptsList {
    private static final String TAG = "ReceiptsList";

    /**
     * Static variable of the ReceiptList. Shouldn't have multiple instances.
     */
    private static ReceiptsList sReceiptsList;

    /**
     * Context of the given activity
     */
    private Context mContext;

    /**
     * SQLiteDatabase of the receipt list
     */
    private SQLiteDatabase mdb;

    public UUID getGroupId() {
        return mGroupId;
    }

    public void setGroupId(UUID groupId) {
        mGroupId = groupId;
    }

    private UUID mGroupId;

    private UUID mUserId;

    /**
     * Non static Receipt List constructor
     *
     * @param context The applicaton context.
     */
    public ReceiptsList(Context context) {
        mContext = context.getApplicationContext();
        mdb = new DbHelper(mContext).getWritableDatabase();
    }

    /**
     * Receipt constructor
     *
     * @param context The applicaton context.
     * @return
     */
    public static ReceiptsList get(Context context) {
        if (sReceiptsList == null) {
            sReceiptsList = new ReceiptsList(context);
        }
        return sReceiptsList;
    }

    public void clearDatabase() {
        Log.i(TAG, "Clearing database");
        mdb.delete(ReceiptsTable.NAME, null, null);
    }

    /**
     * Adds a receipt to the Receipts List
     *
     * @param receipt
     */
    public void addReceipt(Receipt receipt) {
        Log.d(TAG, "addReceipt: Adding receipt in ReceiptsList");
        ContentValues values = getContentValues(receipt);
        mdb.insert(ReceiptsTable.NAME, null, values);
    }

    /**
     * Deletes the receipt @ the location
     *
     * @param receipt
     */
    public void removeReceipt(Receipt receipt) {
        mdb.delete(ReceiptsTable.NAME, ReceiptsTable.Cols.RECEIPT_ID + " = ?", new String[]{receipt.getId().toString()});
    }

    /**
     * @return the receipt list
     */
    public List<Receipt> getReceipts() {
        List<Receipt> receipts = new ArrayList<>();

        String where = ReceiptsTable.Cols.GROUP_ID + " = ?";
        //ReceiptCursorWrapper cursor = queryReceipts(where, new String[]{mGroupId.toString()});
        ReceiptCursorWrapper cursor = queryReceipts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                receipts.add(cursor.getReceipt());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return receipts;
    }

    /**
     * Gets receipt from receipt list
     *
     * @param id The Id of the target receipt
     * @return Receipt
     */
    public Receipt getReceipt(UUID id) {
        ReceiptCursorWrapper cursor = queryReceipts(
                ReceiptsTable.Cols.RECEIPT_ID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getReceipt();
        } finally {
            cursor.close();
        }
    }

    /**
     * Gets the photo file from device
     *
     * @param receipt
     * @return
     */
    public File getPhotoFile(Receipt receipt) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null || receipt == null) {
            return null;
        }

        return new File(externalFilesDir, receipt.getPhotoFilename());
    }

    /**
     * Updates Receipt of a certain receipt
     *
     * @param receipt
     */
    public void updateReceipt(Receipt receipt) {
        String uuidString = receipt.getId().toString();
        ContentValues values = getContentValues(receipt);

        int count = mdb.update(ReceiptsTable.NAME, values,
                ReceiptsTable.Cols.RECEIPT_ID + " = ?", new String[]{uuidString});
        if (count != 1) {
            Log.w(TAG, "Tried to update receipt " + receipt + ", but updated " + count + " rows. Expected 1");
        }
    }

    private ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();

        values.put(ReceiptsTable.Cols.RECEIPT_ID, receipt.getId().toString());
        //values.put(ReceiptsTable.Cols.GROUP_ID, receipt.getGroupId().toString());
        values.put(ReceiptsTable.Cols.TITLE, receipt.getTitle());
        values.put(ReceiptsTable.Cols.DATE, receipt.getDate().getTime());
        values.put(ReceiptsTable.Cols.DAY_OF_YEAR, receipt.getDayOfYear());
        values.put(ReceiptsTable.Cols.TOTAL, receipt.getTotal());

        return values;
    }

    private ReceiptCursorWrapper query(String where, String[] args, String table) {
        Cursor cursor = mdb.query(table,
                null, // Columns - null selects all of them
                where,
                args,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ReceiptCursorWrapper(cursor);
    }

    private ReceiptCursorWrapper queryReceipts(String where, String[] args) {
        return query(where, args, ReceiptsTable.NAME);
    }

    public boolean setUsername(String username) {
        Cursor cursor = mdb.query(UsersTable.NAME,
                null, // Columns - null selects all of them
                UsersTable.Cols.USERNAME + " = ?",
                new String[]{username},
                null, // groupBy
                null, // having
                null // orderBy
        );
        try {
            cursor.moveToFirst();
            if (cursor.getCount() != 1) {
                // We need to make a new user.
                return false;
            }
            int idx = cursor.getColumnIndex(UsersTable.Cols.USER_ID);
            mUserId = UUID.fromString(cursor.getString(idx));
            return true;
        } finally {
            cursor.close();
        }
    }

    public void addUser(String username) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.Cols.USER_ID, UUID.randomUUID().toString());
        values.put(UsersTable.Cols.USERNAME, username);

        mdb.insert(UsersTable.NAME, null, values);
    }
}
