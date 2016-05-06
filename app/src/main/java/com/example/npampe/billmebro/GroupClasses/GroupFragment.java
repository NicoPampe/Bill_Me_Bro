package com.example.npampe.billmebro.GroupClasses;

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

import com.example.npampe.billmebro.R;

import java.util.ArrayList;
import java.util.UUID;

public class GroupFragment extends Fragment {
    private static final String ARG_GROUP_ID = "group_id";
    private Group mGroup;
    private Callbacks mCallbacks;
    private EditText mNameField;
    private EditText mGroupTypeField;
    private EditText mGroupMembersField;

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

        View v = inflater.inflate(R.layout.fragment_group, container, false);

        if (mGroup != null) {
            mNameField = (EditText) v.findViewById(R.id.group_name_edit_text);
            mNameField.setText(mGroup.getName());
            mNameField.addTextChangedListener(new TextWatcher() {
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

            mGroupTypeField = (EditText) v.findViewById(R.id.group_type_edit_text);
            mGroupTypeField.setText(mGroup.getType());
            mGroupTypeField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mGroup.setType(s.toString());
                    updateGroup();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mGroupMembersField = (EditText) v.findViewById(R.id.add_group_members_edit_text);
            ArrayList<String> members = mGroup.getMembers();
            String membersString = "";
            if (members.isEmpty()) {
                mGroupMembersField.setHint(R.string.add_group_members_edit_text_hint);
            }
            else {
                for (int i = 0; i < members.size(); i++) {
                    membersString += members.get(i) + "\n";
                }

                mGroupMembersField.setText(membersString);
            }
            mGroupMembersField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String membersString = String.valueOf(s.subSequence(0, s.length()));
                    String members[] = membersString.split("\\r?\\n");

                    mGroup.clearMembers();

                    for (int i = 0; i < members.length; i++) {
                        mGroup.addMember(members[i]);
                    }
                    updateGroup();
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
    }


    /**
     * Required interface for hositng act
     */
    public interface Callbacks {
        void onGroupUpdated(Group group);
    }
}