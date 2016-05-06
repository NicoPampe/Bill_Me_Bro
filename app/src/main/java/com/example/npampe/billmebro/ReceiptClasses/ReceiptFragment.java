package com.example.npampe.billmebro.ReceiptClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.npampe.billmebro.DatePickerFragment;
import com.example.npampe.billmebro.PictureDialogFragment;
import com.example.npampe.billmebro.PictureUtils;
import com.example.npampe.billmebro.R;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class ReceiptFragment extends Fragment {
    private static final String ARG_RECEIPT_ID = "receipt_id";
    private static final String DIALOG_PICTURE = "DialogPicture";
    private static final int REQUEST_DATE = 0;
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
        // first, photo button
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

        // Next, photo View
        mPhotoView = (ImageView)v.findViewById(R.id.receipt_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                PictureDialogFragment dialog = PictureDialogFragment.newInstance(mPhotoView);
                dialog.show(manager, DIALOG_PICTURE);
            }
        });
        updatePhotoView();

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

    @Override
    public void onPause() {
        super.onPause();
        ReceiptsList.get(getActivity()).updateReceipt(mReceipt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mReceipt.setDate(date);
            updateReceipt();
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            updateReceipt();
            updatePhotoView();
        }
    }

    /**
     * Update the receipts date
     */
    private void updateDate() {
        // TODO: implement updateDate
    }

    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onReceiptUpdated(Receipt receipt);
    }

    /**
     * Updates the Photo of the receipt
     */
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}