package com.example.npampe.billmebro;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by Nick Pampe on 4/9/2016.
 */
public class ReceiptFragment extends Fragment {
    private static final String ARG_RECEIPT_ID = "receipt_id";


    public static ReceiptFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECEIPT_ID, id);

        ReceiptFragment fragment = new ReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onReceiptUpdated(Receipt crime);
    }
}
