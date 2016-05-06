package com.example.npampe.billmebro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptBaseHelper;
import com.example.npampe.billmebro.database.ReceiptCursorWrapper;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

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
        mReceipts = new ArrayList<>();
        mContext = context.getApplicationContext();
        mdb = new ReceiptBaseHelper(mContext).getWritableDatabase();
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

    public void nukeIt() {
        Log.i(TAG, "Nuking database!");
        mContext.deleteDatabase(ReceiptBaseHelper.DATABASE_NAME);
    }

    /**
     * Adds a receipt to the Receipts List
     *
     * @param receipt
     */
    public void addReceipt(Receipt receipt) {
        Log.d(TAG, "addReceipt: Adding receipt in ReceiptsList");
        ContentValues values = getContentValues(receipt);
        mdb.insert(ReceiptTable.NAME, null, values);
    }

    /**
     * Deletes the receipt @ the location
     *
     * @param receipt
     */
    public void removeReceipt(Receipt receipt) {
        mdb.delete(ReceiptTable.NAME, ReceiptTable.Cols.UUID + " = ?", new String[] {receipt.getId().toString()});
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
                ReceiptTable.Cols.UUID + " = ?",
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

        mdb.update(ReceiptTable.NAME, values,
                ReceiptTable.Cols.UUID + " = ?", new String[]{uuidString});
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
        values.put(ReceiptTable.Cols.DAY_OF_YEAR, receipt.getDayOfYear());

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
