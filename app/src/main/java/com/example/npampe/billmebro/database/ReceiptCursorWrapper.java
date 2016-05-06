package com.example.npampe.billmebro.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.npampe.billmebro.ReceiptClasses.Receipt;
import com.example.npampe.billmebro.database.ReceiptDbSchema.ReceiptTable;

import java.util.Date;
import java.util.UUID;

public class ReceiptCursorWrapper extends CursorWrapper {

    public ReceiptCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Receipt getReceipt() {
        String uuidStr = getString(getColumnIndex(ReceiptTable.Cols.UUID));
        String title = getString(getColumnIndex(ReceiptTable.Cols.TITLE));
        long date = getLong(getColumnIndex(ReceiptTable.Cols.DATE));
        double total = getDouble(getColumnIndex(ReceiptTable.Cols.TOTAL));
        int dayOfYear = getInt(getColumnIndex(ReceiptTable.Cols.DAY_OF_YEAR));

        Receipt receipt = new Receipt(UUID.fromString(uuidStr));
        receipt.setTitle(title);
        receipt.setDate(new Date(date));
        receipt.setTotal(total);
        receipt.setDayOfYear(dayOfYear);
        return receipt;
    }
}
