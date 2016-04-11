package com.example.npampe.billmebro;

import android.support.v4.app.Fragment;

public class WelcomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new WelcomeFragment();
    }
}
