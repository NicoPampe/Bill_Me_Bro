package com.example.npampe.billmebro.ReceiptClasses;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.npampe.billmebro.R;

public class ReceiptPreviewDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_receipt_preview, null);
        TextView title = new TextView(getActivity());
        title.setText("Receipt Preview");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setCustomTitle(title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
