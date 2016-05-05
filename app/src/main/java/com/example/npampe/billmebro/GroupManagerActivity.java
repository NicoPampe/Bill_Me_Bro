package com.example.npampe.billmebro;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.util.Log;

public class GroupManagerActivity extends SingleFragmentActivity
        implements GroupListFragment.Callbacks, GroupFragment.Callbacks {
    private static final String TAG = "GROUP_LIST_ACTIVITY";

    /** Creates new GroupListFragment
     *
     * @return
     */
    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "createFragment()");
        return new GroupListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /** Opens detail view of group selected respective to layout used
     *
     * If viewing group in portrait view, crimePagerActivity is started
     * If viewing group in list-detail landscape view, view is replaced with two pane view
     *
     * @param group
     */
    @Override
    public void onGroupSelected(Group group) {

        if (findViewById(R.id.detail_fragment_container) == null) {
            Log.d(TAG, "onGroupSelected() creating portrait view");
            Intent intent = new Intent(this, ReceiptListActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "onGroupSelected() creating list-detail view");
//            boolean isInLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

            Fragment newDetail = new ReceiptListActivity().createFragment();

            getSupportFragmentManager().beginTransaction()
                     .replace(R.id.detail_fragment_container, newDetail)
                     .commit();
        }
    }

    /** Updates GroupListFragments UI
     *
     * @param group
     */
    @Override
    public void onGroupUpdated(Group group) {
        Log.d(TAG, "onCrimeUpdated()");
        GroupListFragment listFragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
