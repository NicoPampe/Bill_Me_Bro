package com.example.npampe.billmebro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class GroupFragment extends Fragment {
    private static final String ARG_GROUP_ID = "group_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;

    private Group mGroup;
    private Callbacks mCallbacks;
    private EditText mTitleField;

    public static GroupFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP_ID, id);

        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID groupID = (UUID) getArguments().getSerializable(ARG_GROUP_ID);
        mGroup = GroupsList.get(getActivity()).getGroup(groupID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_receipt, container, false);

        if (mGroup != null) {
            mTitleField = (EditText) v.findViewById(R.id.receipt_title_text_view);
            mTitleField.setText(mGroup.getName());
            mTitleField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mGroup.setName(s.toString());
                    updateGroup();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        return v;
    }

    private void updateGroup() {
        GroupsList.get(getActivity()).updateGroup(mGroup);
        mCallbacks.onGroupUpdated(mGroup);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mGroup.setDate(date);
            updateGroup();
            updateDate();
        }
    }

    /**
     * Update the receipts date
     */
    private void updateDate() {
        // TODO: implement updateDate
    }

    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onGroupUpdated(Group group);
    }
}