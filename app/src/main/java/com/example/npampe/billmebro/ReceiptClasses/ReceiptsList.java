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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Receipts List contains the List of Receipts in the group.
 * Methods to modify and receive information about the list.
 */
public class ReceiptsList {
    private static final String TAG = "ReceiptsList";

    /**
     * Static variable of the ReceiptList. Shouldn't have multiple instances.
     */
    private static ReceiptsList sReceiptsList;

    /**
     * List of receipts in the view.
     */
    private List<Receipt> mReceipts;
    /**
     * Context of the given activity
     */
    private Context mContext;
    /**
     * SQLiteDatabase of the receipt list
     */
    private SQLiteDatabase mdb;

    /**
     * Non static Receipt List constructor
     *
     * @param context The applicaton context.
     */
    public ReceiptsList(Context context) {
        Log.i(TAG, "ReceiptsList()");
        mReceipts = new ArrayList<>();
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
        Log.i(TAG, "Nuking database!");
        mdb.delete(ReceiptsTable.NAME, null, null);
        mReceipts.clear();
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
        mdb.delete(ReceiptsTable.NAME, ReceiptsTable.Cols.RECEIPT_ID + " = ?", new String[] {receipt.getId().toString()});
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
     * @return the size of the receipt list
     */
    public int receiptCound() {
        return mReceipts.size();
    }

    /**
     * Gets receipt from receipt list
     *
     * @param id The Id of the target receipt
     * @return Receipt
     */
    public Receipt getReceipt(UUID id) {
        ReceiptCursorWrapper cursor = query(
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
     * @return returns the database of the Receipt List
     */
    public SQLiteDatabase getDataBase() {
        return mdb;
    }

    /**
     * Gets the photo file from device
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

        mdb.update(ReceiptsTable.NAME, values,
                ReceiptsTable.Cols.RECEIPT_ID + " = ?", new String[]{uuidString});
    }

    /**
     * Get's the context from the Db Table on the receipts
     *
     * @param receipt
     * @return
     */
    private ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();

        values.put(ReceiptsTable.Cols.RECEIPT_ID, receipt.getId().toString());
        values.put(ReceiptsTable.Cols.TITLE, receipt.getTitle());
        values.put(ReceiptsTable.Cols.DATE, receipt.getDate().getTime());
        values.put(ReceiptsTable.Cols.TOTAL, receipt.getTotal());
        values.put(ReceiptsTable.Cols.DAY_OF_YEAR, receipt.getDayOfYear());

        return values;
    }

    private ReceiptCursorWrapper query(String where, String[] args) {
        Cursor cursor = mdb.query(ReceiptsTable.NAME,
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
