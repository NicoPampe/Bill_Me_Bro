package com.example.npampe.billmebro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * Receipts List contains the List of Receipts in the group.
 * Methods to modify and receive information about the list.
 */
public class ReceiptsList {
    private static final String TAG = "Receipts_List";
    private static ReceiptsList sReceiptsList;

    private List<Receipt> mReceipts;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * Non static Receipt List constructor
     * @param context
     */
    public ReceiptsList(Context context) {
        mReceipts = new ArrayList<>();
        mContext = context.getApplicationContext();
        mDatabase = new ReceiptBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Receipt constructor
     * @param context
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
     * @param r
     */
    public void addReceipt(Receipt r) {
        Log.d(TAG, "addReceipt: " + r.getTitle());
        mReceipts.add(r);
    }

    /**
     * Deletes the receipt @ the location
     * @param r
     */
    public void removeReceipt(Receipt r) {
        mReceipts.remove(mReceipts.indexOf(r));
    }

    /**
     * @return the receipt list
     */
    public List<Receipt> getReceipts() {
        return mReceipts;
    }

    /**
     * @return size of the receipts list
     */
    public int receiptCount() {
        return mReceipts.size();
    }

    /**
     * Gets receipt from receipt list
     * @param id The Id of the target receipt
     * @return Receipt
     */
    public Receipt getReceipt(UUID id) {
        for (Receipt receipt : mReceipts) {
            if (receipt.getId().equals(id)) {
                Log.d(TAG, "getReceipt: uuid = " + id);
                return  receipt;
            }
        }
        return null;
    }

    /**
     * Updates Receipt of a certain receipt
     * @param receipt
     */
    public void updateReceipt(Receipt receipt) {
        for (Receipt thisReceitp : mReceipts) {
            if (thisReceitp.getId() == receipt.getId()) {
                int pos = mReceipts.indexOf(thisReceitp);
                mReceipts.set(pos, receipt);
            }
        }
    }

    /**
     * Get's the context from the Db Table on the receipts
     * @param receipt
     * @return
     */
    private ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();
        values.put(ReceiptDbSchema.ReceiptTable.Cols.UUID, receipt.getId().toString());
        values.put(ReceiptDbSchema.ReceiptTable.Cols.TITLE, receipt.getTitle());
        values.put(ReceiptDbSchema.ReceiptTable.Cols.DATE, receipt.getDate().getTime());
        values.put(ReceiptDbSchema.ReceiptTable.Cols.TOTOAL, receipt.getTotal());

        return values;
    }

    public void clearReceipts() {
        mReceipts.clear();
    }
}
