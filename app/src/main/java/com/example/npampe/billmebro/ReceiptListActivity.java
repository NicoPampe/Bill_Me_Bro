package com.example.npampe.billmebro;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

public class ReceiptListActivity extends SingleFragmentActivity
        implements ReceiptFragment.Callbacks, ReceiptListFragment.Callbacks {

    private static final String TAG = "Receipt_List_Activity";
    private static final String ARG_GROUP_ID = "group_id";

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
            Fragment newRecetiptDetails = ReceiptFragment.newInstance(receipt.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newRecetiptDetails).commit();
        }
    }

    @Override
    public void onReceiptUpdated(Receipt receipt) {
        ReceiptListFragment listFragment = (ReceiptListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
