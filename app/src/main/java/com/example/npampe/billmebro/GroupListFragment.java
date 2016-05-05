package com.example.npampe.billmebro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class GroupListFragment extends Fragment {
    private static final String TAG = "CRIME_LIST_FRAGMENT";
    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private ImageButton mEmptyNewGroupButton;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onGroupSelected(Group group);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        if (!GroupsList.get(getActivity()).getGroups().isEmpty()) {
            setHasOptionsMenu(true);
        }
    }

    /** Inflates view respective to number of crimes
     *
     * If CrimeLab.getGroups() is empty, inflate empty_group_list
     * IF CrimeLab.getGroups() is NOT empty, initialize recyclerView and
     * inflate proper layout
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if (GroupsList.get(getActivity()).getGroups().isEmpty()) {
            Log.d(TAG, "onCreateView(): Empty List");
            view = inflater.inflate(R.layout.empty_group_list, container, false);

            mEmptyNewGroupButton = (ImageButton) view.findViewById(R.id.empty_list_new_crime);
            mEmptyNewGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Group group = new Group("Test Group");
                    GroupsList.get(getActivity()).addGroup(group);

                    Intent intent = new Intent(getActivity(), ReceiptListActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            Log.d(TAG, "onCreateView(): Populated List");
            view = inflater.inflate(R.layout.fragment_group_list, container, false);
            mGroupRecyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
            mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (savedInstanceState != null) {
                mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            }
            updateUI();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // if no crimes exist do not updateUI
        if (!GroupsList.get(getActivity()).getGroups().isEmpty()) {
            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save state of subtitles
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /** Create and inflate fragment_group_list
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_group_list, menu);
    }

    /** Executes logic respective to menuitem selected (add)
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_group:
                Group group = new Group("Test Group");
                GroupsList.get(getActivity()).addGroup(group);
                updateUI();
                mCallbacks.onGroupSelected(group);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Creates crimeRecyclerView's adapter or updates recyclerView if adapter already exists.
     *
     */
    public void updateUI() {
        GroupsList groupManager = GroupsList.get(getActivity());
        List<Group> groups = groupManager.getGroups();

        if (mAdapter == null) {
            mAdapter = new GroupAdapter(groups);
            mGroupRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /** Gets information relative to crime and binds the crimes information to respective UI components
     *
     */
    private class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Group mGroup;
        private TextView mNameTextView;
        private TextView mDateCreatedTextView;

        public GroupHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_group_name_text_view);
            mDateCreatedTextView = (TextView)
                    itemView.findViewById(R.id.list_item_group_date_text_view);
        }

        public void bindGroup(Group group) {
            mGroup = group;
            mNameTextView.setText(mGroup.getName());
            mDateCreatedTextView.setText(mGroup.getDate(0));
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onGroupSelected(mGroup);
        }
    }

    /** Creates CrimeHolders and Binds them to respective crime in CrimeLab
     *
     */
    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {
        private List<Group> mGroups;

        public GroupAdapter(List<Group> groups) {
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_group, parent, false);
            return new GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            Group group = mGroups.get(position);
            holder.bindGroup(group);
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }
    }
}
