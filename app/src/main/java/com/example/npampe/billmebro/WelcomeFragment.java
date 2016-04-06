package com.example.npampe.billmebro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeFragment extends Fragment {
    private static final String TAG = "Welcome_Fragment";
    @Bind(R.id.user_name_text_view)
    TextView mUsernameTextView;

    @Bind(R.id.password_text_view)
    TextView mPasswordTextView;

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
                Toast.makeText(getActivity(), "Thanks Obama!", Toast.LENGTH_LONG).show();
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
