package com.example.npampe.billmebro;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class ReceiptListActivity extends SingleFragmentActivity implements ReceiptFragment.Callbacks, ReceiptListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new ReceiptListFragment();
    }

    @Override
    public void onReceiptSelected(Receipt receipt) {
        if (findViewById(R.id.edit_receipt_fragment) == null) {
            Intent intent = ReceiptPagerActivity.newIntent(this, receipt.getId());
            startActivity(intent);
        } else {
            Fragment newRecetiptDetails = ReceiptFragment.newInstance(receipt.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_receipt_fragment, newRecetiptDetails).commit();
        }
    }

    @Override
    public void onReceiptUpdated(Receipt receipt) {
        ReceiptListFragment listFragment = (ReceiptListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activiity_twopane;
    }
}
