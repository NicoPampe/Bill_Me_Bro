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
        mReceipts.add(receipt);
    }

    /**
     * Deletes the receipt @ the location
     *
     * @param receipt
     */
    public void removeReceipt(Receipt receipt) {
        mReceipts.remove(mReceipts.indexOf(receipt));
    }

    /**
     * @return the receipt list
     */
    public List<Receipt> getReceipts() {
        return mReceipts;
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
        for (Receipt receipt : mReceipts) {
            if (receipt.getId().equals(id)) {
                return receipt;
            }
        }
        return null;
    }

    /**
     * Gets the photo file from device
     * @param receipt
     * @return
     */
    public File getPhotoFile(Receipt receipt) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
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
        Log.i(TAG, "Updating database with " + receipt);
        for (Receipt thisRecpit : mReceipts) {
            if (thisRecpit.getId() == receipt.getId()) {
                int location = mReceipts.indexOf(thisRecpit);
                mReceipts.set(location, receipt);
            }
        }
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
