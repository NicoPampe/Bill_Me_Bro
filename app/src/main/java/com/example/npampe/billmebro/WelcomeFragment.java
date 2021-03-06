package com.example.npampe.billmebro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npampe.billmebro.GroupClasses.GroupManagerActivity;
import com.example.npampe.billmebro.ReceiptClasses.ReceiptsList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeFragment extends Fragment {
    private static final String TAG = "Welcome_Fragment";
    @Bind(R.id.user_name_text_view)
    TextView mUsernameTextView;

    @Bind(R.id.login_button)
    Button mLogInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.acitivity_welcome, container, false);
        ButterKnife.bind(this, view);

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameTextView.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a username.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                    startActivity(i);
                    return;
                }
                ReceiptsList receipts = ReceiptsList.get(getActivity());
                boolean userExists = receipts.setUsername(username);
                if (!userExists) {
                    receipts.addUser(username);
                }
                Intent intent = new Intent(getActivity(), GroupManagerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}