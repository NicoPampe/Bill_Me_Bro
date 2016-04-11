package com.example.npampe.billmebro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;


public class ReceiptPagerActivity extends AppCompatActivity implements ReceiptFragment.Callbacks {
    private static final String EXTRA_RECEIPT_ID = "com.example.npmape.criminalintent.crime_id";
    private static final String TAG = "Receipt_Pager_Activity";
    private ViewPager mViewPager;
    private List<Receipt> mReceipts;

    public static Intent newIntent(Context packageContext, UUID receiptUUID) {
        Intent intent = new Intent(packageContext, ReceiptPagerActivity.class);
        intent.putExtra(EXTRA_RECEIPT_ID, receiptUUID);
        return intent;
    }

    /**
     * Creates the pager view
     * onCreate uses the Fragment Manager
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_pager);

        UUID crimeID = (UUID)getIntent().getSerializableExtra(EXTRA_RECEIPT_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_receipt_pager_view_pager);

        mReceipts = ReceiptsList.get(this).getReceipts();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Receipt receipt = mReceipts.get(position);
                return ReceiptFragment.newInstance(receipt.getId());
            }

            @Override
            public int getCount() {
                return mReceipts.size();
            }
        });

        for (int i = 0; i < mReceipts.size(); i++) {
            if (mReceipts.get(i).getId().equals(crimeID)) {
                mViewPager.setCurrentItem(i);
                Log.d(TAG, "onCreate: mReceipt's UUID = " + crimeID);
                break;
            }
        }


    }

    @Override
    public void onReceiptUpdated(Receipt receipt) {
    }
}
