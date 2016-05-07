package com.example.npampe.billmebro;

import android.support.v4.app.Fragment;

import com.example.npampe.billmebro.ReceiptClasses.ReceiptsList;

public class WelcomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        // Make sure the database loads BEFORE the fragment.
        ReceiptsList.get(this);
        return new WelcomeFragment();
    }
}
