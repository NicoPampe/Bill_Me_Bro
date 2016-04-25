package com.example.npampe.billmebro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.util.UUID;

public class ReceiptFragment extends Fragment {
    private static final String ARG_RECEIPT_ID = "receipt_id";
    private static final int REQUEST_PHOTO = 2;

    private Receipt mReceipt;
    private File mPhotoFile;
    private Callbacks mCallbacks;
    private EditText mTitleField;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;


    public static ReceiptFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECEIPT_ID, id);

        ReceiptFragment fragment = new ReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_RECEIPT_ID);
        mReceipt = ReceiptsList.get(getActivity()).getReceipt(crimeID);
        mPhotoFile = ReceiptsList.get(getActivity()).getPhotoFile(mReceipt);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_receipt, container, false);

        if (mReceipt != null) {
            mTitleField = (EditText) v.findViewById(R.id.receipt_title_text_view);
            mTitleField.setText(mReceipt.getTitle());
            mTitleField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mReceipt.setTitle(s.toString());
                    updateReceipt();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        /**
         * Photo creation and fire intent to take photo
         */
        mPhotoButton = (ImageButton)v.findViewById(R.id.receipt_camera);
        final Intent caputreImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null && caputreImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            caputreImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(caputreImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.receipt_photo);

        return v;
    }

    private void updateReceipt() {
        ReceiptsList.get(getActivity()).updateReceipt(mReceipt);
        mCallbacks.onReceiptUpdated(mReceipt);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onReceiptUpdated(Receipt receipt);
    }
}