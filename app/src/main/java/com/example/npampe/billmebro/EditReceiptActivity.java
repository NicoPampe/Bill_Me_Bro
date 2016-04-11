package com.example.npampe.billmebro;

import android.support.v4.app.Fragment;

public class EditReceiptActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new EditReceiptFragment();
    }
}
