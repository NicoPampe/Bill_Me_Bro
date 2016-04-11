package com.example.npampe.billmebro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.npampe.billmebro.database.ReceiptBaseHelper;
import com.example.npampe.billmebro.database.ReceiptDbSchema;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

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
     * @param context The applicaton context.
     */
    public ReceiptsList(Context context) {
        mContext = context.getApplicationContext();
        mdb = new ReceiptBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void nukeIt() {
        Log.i(TAG, "Deleting database!");
        mContext.deleteDatabase(ReceiptBaseHelper.DATABASE_NAME);
    }

    /**
     * Receipt constructor
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
     * @param receipt
     */
    public void addReceipt(Receipt receipt) {
        ContentValues values = getContentValues(receipt);
        mdb.insert(ReceiptTable.NAME, null, values);
    }

    /**
     * Deletes the receipt @ the location
     * @param r
     */
    public void removeReceipt(Receipt r) {
    }

    /**
     * @return the receipt list
     */
    public List<Receipt> getReceipts() {
        return null;
    }

    /**
     * @return size of the receipts list
     */
    public int receiptCount() {
        return 0;
    }

    /**
     * Gets receipt from receipt list
     * @param id The Id of the target receipt
     * @return Receipt
     */
    public Receipt getReceipt(UUID id) {
        return null;
    }

    /**
     * Updates Receipt of a certain receipt
     * @param receipt
     */
    public void updateReceipt(Receipt receipt) {
        String uuidStr = receipt.getId().toString();
        ContentValues values = getContentValues(receipt);

        mdb.update(ReceiptTable.NAME, values,
                ReceiptTable.Cols.UUID + " = ?",
                new String[] {uuidStr});
    }

    /**
     * Get's the context from the Db Table on the receipts
     * @param receipt
     * @return
     */
    private ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();

        values.put(ReceiptTable.Cols.UUID,  receipt.getId().toString());
        values.put(ReceiptTable.Cols.TITLE, receipt.getTitle());
        values.put(ReceiptTable.Cols.DATE, receipt.getDate().getTime());
        values.put(ReceiptTable.Cols.TOTAL, receipt.getTotal());

        return values;
    }

    public void clearReceipts() {

    }
}
