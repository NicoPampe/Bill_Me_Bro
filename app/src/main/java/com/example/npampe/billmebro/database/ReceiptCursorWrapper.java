package com.example.npampe.billmebro.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.npampe.billmebro.ReceiptClasses.Receipt;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptsTable;

import java.util.Date;
import java.util.UUID;

public class ReceiptCursorWrapper extends CursorWrapper {

    public ReceiptCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Receipt getReceipt() {
        String uuidStr = getString(getColumnIndex(ReceiptsTable.Cols.RECEIPT_ID));
        String title = getString(getColumnIndex(ReceiptsTable.Cols.TITLE));
        long date = getLong(getColumnIndex(ReceiptsTable.Cols.DATE));
        double total = getDouble(getColumnIndex(ReceiptsTable.Cols.TOTAL));
        int dayOfYear = getInt(getColumnIndex(ReceiptsTable.Cols.DAY_OF_YEAR));

        Receipt receipt = new Receipt(UUID.fromString(uuidStr));
        receipt.setTitle(title);
        receipt.setDate(new Date(date));
        receipt.setTotal(total);
        receipt.setDayOfYear(dayOfYear);
        return receipt;
    }
}
