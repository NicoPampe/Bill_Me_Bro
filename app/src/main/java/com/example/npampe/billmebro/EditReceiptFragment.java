package com.example.npampe.billmebro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class EditReceiptFragment extends Fragment {

    private static final String TAG = "EditReceiptFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.edit_receipt_fragment, container, false);

        ReceiptsList rl = new ReceiptsList(getActivity());

        List<Receipt> receipts = Arrays.asList(new Receipt("Receipt A"),
                new Receipt("Receipt B"),
                new Receipt("Receipt C"),
                new Receipt("Receipt D"));
        for (Receipt receipt : receipts) {
            rl.addReceipt(receipt);
        }

        rl.addReceipt(receipts.get(0));
        rl.removeReceipt(receipts.get(0));

        for (Receipt receipt : rl.getReceipts()) {
            Log.i(TAG, receipt.toString());
        }
        rl.nukeIt();

        return view;
    }

}
