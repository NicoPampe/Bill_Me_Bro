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
        return R.layout.activity_fragment;
    }

    /** Opens detail view of group selected
     *
     * @param group
     */
    @Override
    public void onGroupSelected(Group group) {
        Intent intent = GroupPagerActivity.newIntent(this, group.getId());
        startActivity(intent);
    }

    /** Updates GroupListFragments UI
     *
     * @param group
     */
    @Override
    public void onGroupUpdated(Group group) {
        Log.d(TAG, "onGroupUpdated()");
        GroupListFragment listFragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
