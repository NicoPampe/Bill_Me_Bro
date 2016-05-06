package com.example.npampe.billmebro.ReceiptClasses;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.npampe.billmebro.R;
import com.example.npampe.billmebro.SingleFragmentActivity;

public class ReceiptListActivity extends SingleFragmentActivity
        implements ReceiptFragment.Callbacks, ReceiptListFragment.Callbacks {

    private static final String TAG = "Receipt_List_Activity";
    private static final String ARG_GROUP_ID = "group_id";
    private static final String FRAGMENT_TAG = "Receipt_List_Fragment";

    @Override
    protected Fragment createFragment() {
        return new ReceiptListFragment();
    }

    @Override
    public void onReceiptSelected(Receipt receipt) {
        Log.d(TAG, "onReceiptSelected: checking");
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = ReceiptPagerActivity.newIntent(this, receipt.getId());
            startActivity(intent);
        } else {
            Log.d(TAG, "onReceiptSelected: Updated the edit receipt frag");
            Fragment newReceiptDetails = ReceiptFragment.newInstance(receipt.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newReceiptDetails).commit();
        }
    }

    @Override
    public void onReceiptUpdated(Receipt receipt) {
        Log.d(TAG, "onReceiptUpdated: ");
        ReceiptListFragment listFragment = (ReceiptListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
        getSupportFragmentManager().beginTransaction().remove(listFragment).add(R.id.fragment_container, createFragment()).commit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
