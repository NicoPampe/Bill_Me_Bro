package com.example.npampe.billmebro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiptEditFragment extends Fragment {

    private static final String TAG = "ReceiptEditFragment";
    private static final String INTENT_EXTRA_RECEIPT = "receipt";

    @Bind(R.id.edit_receipt_title)
    EditText mEditTextTitle;

    private Receipt mReceipt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mReceipt = (Receipt) getActivity().getIntent().getSerializableExtra(INTENT_EXTRA_RECEIPT);

        View view = inflater.inflate(R.layout.edit_receipt_fragment, container, false);

        ButterKnife.bind(this, view);

        mEditTextTitle.setText(mReceipt.getTitle());

        return view;
    }

    public static Intent newInstance(Context context, Receipt receipt) {
        Intent i = new Intent(context, ReceiptEditActivity.class);
        i.putExtra(INTENT_EXTRA_RECEIPT, receipt);
        return i;
    }
}
