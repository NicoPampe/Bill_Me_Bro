package com.example.npampe.billmebro.ReceiptClasses;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.npampe.billmebro.R;

import java.util.List;

/**
 * Created by Nick Pampe on 5/6/2016.
 */
public class ReceiptProjectSummarizeDialog extends DialogFragment {
    private List<Receipt> mReceipts;

    private TextView mTotal;


    public ReceiptProjectSummarizeDialog(List<Receipt> receipts) {
        mReceipts = receipts;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_summarized_project, null);

        initTextView(v);
        updateTextViews();

        TextView title = new TextView(getActivity());
        title.setText("Summarization of the Project");
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

    private void updateTextViews() {
        double sum = 0;
        for (Receipt receipt : mReceipts) {
            sum += receipt.getTotal();
        }
        mTotal.setText(Double.toString(sum));
    }

    private void initTextView(View v) {
        mTotal = (TextView)v.findViewById(R.id.preview_total_on_project);
    }
}
