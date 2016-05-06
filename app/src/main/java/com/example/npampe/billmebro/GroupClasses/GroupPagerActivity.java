package com.example.npampe.billmebro.GroupClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.npampe.billmebro.R;

import java.util.List;
import java.util.UUID;

public class GroupPagerActivity extends AppCompatActivity implements GroupFragment.Callbacks {
    private static final String EXTRA_GROUP_ID = "com.example.npmape.billmebro";
    private static final String TAG = "Receipt_Pager_Activity";
    private ViewPager mViewPager;
    private List<Group> mGroups;

    public static Intent newIntent(Context packageContext, UUID groupUUID) {
        Intent intent = new Intent(packageContext, GroupPagerActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, groupUUID);
        return intent;
    }

    /**
     * Creates the pager view
     * onCreate uses the Fragment Manager
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pager);

        UUID groupID = (UUID) getIntent().getSerializableExtra(EXTRA_GROUP_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_group_pager_view_pager);

        mGroups = GroupsList.get(this).getGroups();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Group group = mGroups.get(position);
                return GroupFragment.newInstance(group.getId());
            }

            @Override
            public int getCount() {
                return mGroups.size();
            }
        });

        for (int i = 0; i < mGroups.size(); i++) {
            if (mGroups.get(i).getId().equals(groupID)) {
                mViewPager.setCurrentItem(i);
                Log.d(TAG, "onCreate: mGroups's UUID = " + groupID);
                break;
            }
        }
    }

    @Override
    public void onGroupUpdated(Group group) {
    }
}
