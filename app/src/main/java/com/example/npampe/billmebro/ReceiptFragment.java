package com.example.npampe.billmebro;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by Nick Pampe on 4/9/2016.
 */
public class ReceiptFragment extends Fragment {
    private static final String ARG_RECEIPT_ID = "receipt_id";

    private Receipt mReceipt;
    private Callbacks mCallbacks;


    public static ReceiptFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECEIPT_ID, id);

        ReceiptFragment fragment = new ReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_RECEIPT_ID);
        mReceipt = ReceiptsList.get(getActivity()).getReceipt(crimeID);

        setHasOptionsMenu(true);
    }

    private void updateCrime() {
        ReceiptsList.get(getActivity()).updateReceipt(mReceipt);
        mCallbacks.onReceiptUpdated(mReceipt);
    }

    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onReceiptUpdated(Receipt receipt);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}