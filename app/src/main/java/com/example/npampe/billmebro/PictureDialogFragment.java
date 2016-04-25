package com.example.npampe.billmebro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Nick Pampe on 2/29/2016.
 */
public class PictureDialogFragment extends DialogFragment {
    private static final String ARG_PICTURE = "picture";

    private ImageView mImageView;

    public static PictureDialogFragment newInstance(ImageView imageView) {
        Bundle args = new Bundle();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        args.putParcelable(ARG_PICTURE, bitmap);

        PictureDialogFragment fragment = new PictureDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_picture, null);

        Bitmap bmp = (Bitmap) getArguments().getParcelable(ARG_PICTURE);
        mImageView = (ImageView)v.findViewById(R.id.dialog_picture);
        mImageView.setImageBitmap(bmp);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.photo_dialog_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
