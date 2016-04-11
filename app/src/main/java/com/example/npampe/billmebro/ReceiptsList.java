package com.example.npampe.billmebro;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptBaseHelper;
import com.example.npampe.billmebro.database.ReceiptCursorWrapper;
import com.example.npampe.billmebro.database.ReceiptDbSchema;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * Receipts List contains the List of Receipts in the group.
 * Methods to modify and receive information about the list.
 */
public class ReceiptsList {

    private static final String TAG = "ReceiptsList";

    private static ReceiptsList sReceiptsList;

    private Context mContext;
    private SQLiteDatabase mdb;

    /**
     * Non static Receipt List constructor
     *
     * @param context The applicaton context.
     */
    public ReceiptsList(Context context) {
        mContext = context.getApplicationContext();
        mdb = new ReceiptBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void nukeIt() {
        Log.i(TAG, "Nuking database!");
        mContext.deleteDatabase(ReceiptBaseHelper.DATABASE_NAME);
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

    /**
     * Adds a receipt to the Receipts List
     *
     * @param receipt
     */
    public void addReceipt(Receipt receipt) {
        if (getReceipt(receipt.getId()) != null) {
            updateReceipt(receipt);
        } else {
            Log.i(TAG, "Inserting into database: " + receipt);
            ContentValues values = getContentValues(receipt);
            mdb.insert(ReceiptTable.NAME, null, values);
        }
    }

    /**
     * Deletes the receipt @ the location
     *
     * @param r
     */
    public void removeReceipt(Receipt receipt) {
        int updated = mdb.delete(ReceiptTable.NAME,
                ReceiptTable.Cols.UUID + " = ?",
                new String[] {receipt.getId().toString() });
        Log.i(TAG, "Removed " + updated + " rows with " + receipt);
    }

    /**
     * @return the receipt list
     */
    public List<Receipt> getReceipts() {
        List<Receipt> receipts = new ArrayList<>();

        ReceiptCursorWrapper cursor = query(null, null);
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
     * @return size of the receipts list
     */
    public int receiptCount() {
        return 0;
    }

    /**
     * Gets receipt from receipt list
     *
     * @param id The Id of the target receipt
     * @return Receipt
     */
    public Receipt getReceipt(UUID id) {
        ReceiptCursorWrapper cursor = query(ReceiptTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                return cursor.getReceipt();
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    /**
     * Updates Receipt of a certain receipt
     *
     * @param receipt
     */
    public void updateReceipt(Receipt receipt) {
        Log.i(TAG, "Updating database with " + receipt);
        String uuidStr = receipt.getId().toString();
        ContentValues values = getContentValues(receipt);

        mdb.update(ReceiptTable.NAME, values,
                ReceiptTable.Cols.UUID + " = ?",
                new String[]{uuidStr});
    }

    /**
     * Get's the context from the Db Table on the receipts
     *
     * @param receipt
     * @return
     */
    private ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();

        values.put(ReceiptTable.Cols.UUID, receipt.getId().toString());
        values.put(ReceiptTable.Cols.TITLE, receipt.getTitle());
        values.put(ReceiptTable.Cols.DATE, receipt.getDate().getTime());
        values.put(ReceiptTable.Cols.TOTAL, receipt.getTotal());

        return values;
    }

    private ReceiptCursorWrapper query(String where, String[] args) {
        Cursor cursor = mdb.query(ReceiptTable.NAME,
                null, // Columns - null selects all of them
                where,
                args,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ReceiptCursorWrapper(cursor);
    }
}
