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

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiptPreviewDialog extends DialogFragment {
    private Receipt mReceipt;

    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private TextView mLocationTextView;
    private TextView mContributors;
    private TextView mConficts;

    public ReceiptPreviewDialog() {
        mReceipt = new Receipt();
    }

    public ReceiptPreviewDialog(Receipt receipt) {
        mReceipt = receipt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_receipt_preview, null);

        initTextView(v);
        updateTextViews();

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

    public void initTextView(View view) {
        mTitleTextView = (TextView)view.findViewById(R.id.preview_title_text_view);
        mDateTextView = (TextView)view.findViewById(R.id.preview_date_text_view);
        mTimeTextView = (TextView)view.findViewById(R.id.preview_time_text_view);
        mLocationTextView = (TextView)view.findViewById(R.id.preview_location_text_view);
        mContributors = (TextView)view.findViewById(R.id.preview_contributors_text_view);
        mConficts = (TextView)view.findViewById(R.id.preview_conflicts_text_view);
    }

    public void updateTextViews() {
        mTitleTextView.setText(mReceipt.getTitle());
        mDateTextView.setText(mReceipt.getDate().toString());
        // TODO: update to just be the mont and day
//        mDateTextView.setText(String.format("%d %d", mReceipt.getDate().getMonth(), mReceipt.getDate().getDay()));
        mTimeTextView.setText(String.format("%d",mReceipt.getDate().getTime()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
