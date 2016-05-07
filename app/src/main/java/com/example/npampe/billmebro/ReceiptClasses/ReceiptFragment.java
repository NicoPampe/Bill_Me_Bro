package com.example.npampe.billmebro.ReceiptClasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.npampe.billmebro.DatePickerFragment;
import com.example.npampe.billmebro.PictureDialogFragment;
import com.example.npampe.billmebro.PictureUtils;
import com.example.npampe.billmebro.R;

import java.io.File;
import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;

public class ReceiptFragment extends Fragment {
    private static final String TAG = "ReceiptFragment";
    private static final String ARG_RECEIPT_ID = "receipt_id";
    private static final String DIALOG_PICTURE = "DialogPicture";
    private static final String DIALOG_PREVIEW = "DialogPreview";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;

    private Receipt mReceipt;
    private File mPhotoFile;
    private Callbacks mCallbacks;
    private EditText mTitleField;

    public TextView mReceiptTotalTextView;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mDateButton;
    private EditText mLocationEditText;


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

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mReceipt.setTitle(s.toString());
                    updateReceipt();
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

        /**
         * Set money picker
         * An extra action is set up for a responce from the listener.
         * Calls an action dialog.
         */
        mReceiptTotalTextView = (TextView)v.findViewById(R.id.receipt_total_price);
        mReceiptTotalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View theView = inflater.inflate(R.layout.money_picker, null);
                //I define the dialog and I load the xml layout: number_picker_dialog.xml into the view

                final NumberPicker unit_euro = (NumberPicker) theView.findViewById(R.id.bill_picker);
                final NumberPicker cent = (NumberPicker) theView.findViewById(R.id.cent_picker);
                // I keep a reference to the 2 picker, in order to read their properties for later use

                builder.setView(theView)
                        .setPositiveButton(R.string.accept_price_change, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("DBG", "Price is: " + unit_euro.getValue() + "." + cent.getValue());
//                                Log.d(TAG, "onClick: " + Double.toString(cent.getValue() * 5));
                                mReceipt.setTotal(unit_euro.getValue() + (cent.getValue() * 5 / 100.0));
                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                String resultTotal = formatter.format(mReceipt.getTotal());
                                mReceiptTotalTextView.setText(resultTotal);
                            }
                        }).setNegativeButton(R.string.reject_price_change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // I define 2 default buttons, with 2 strings (accept_price_change and reject_price_change) and their behaviours

                unit_euro.setMinValue(0);
                unit_euro.setMaxValue(100);
                // I define the range for the first numberpicker.

                String cents[] = new String[20];
                for (int i = 0; i < 100; i += 5) {
                    if (i < 10)
                        cents[i / 5] = "0" + i;
                    else
                        cents[i / 5] = "" + i;
                }
                cent.setDisplayedValues(cents);
                //I create the range of the possible values displayed in the second numberpicker.

                cent.setMinValue(0);
                cent.setMaxValue(19);
                cent.setValue(0);
                //I configure the possible values of the second picker

                builder.show();
                //Finally, the alert is showed!
            }
        });

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String resultTotal = formatter.format(mReceipt.getTotal());
        mReceiptTotalTextView.setText(resultTotal);

        mDateButton = (Button) v.findViewById(R.id.receipt_date_picker);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mReceipt.getDate());
                dialog.setTargetFragment(ReceiptFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

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
        mDateButton.setText(mReceipt.getFormattedDate());
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