package com.example.npampe.billmebro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.npampe.billmebro.ReceiptClasses.ReceiptFragment;

/**
 * Created by Nick Pampe on 5/6/2016.
 */
public class MoneyDialog extends DialogFragment {
    public NumberPicker unit_euro;
    public NumberPicker cent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View theView = inflater.inflate(R.layout.money_picker, null);
        //I define the dialog and I load the xml layout: number_picker_dialog.xml into the view

        unit_euro = (NumberPicker) theView.findViewById(R.id.bill_picker);
        cent = (NumberPicker) theView.findViewById(R.id.cent_picker);
        // I keep a reference to the 2 picker, in order to read their properties for later use

        builder.setView(theView)
                .setPositiveButton(R.string.accept_price_change,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("DBG", "Price is: " + unit_euro.getValue() + "." + cent.getValue());
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
        for(int i = 0;i < 100; i+=5) {
            if( i < 10 )
                cents[i/5] = "0"+i;
            else
                cents[i/5] = ""+i;
        }
        cent.setDisplayedValues(cents);
        //I create the range of the possible values displayed in the second numberpicker.

        cent.setMinValue(0);
        cent.setMaxValue(19);
        cent.setValue(0);
        //I configure the possible values of the second picker

        builder.show();
        //Finally, the alert is showed!

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
