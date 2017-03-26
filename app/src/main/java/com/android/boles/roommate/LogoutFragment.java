package com.android.boles.roommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by boles on 2/14/2017.
 */

public class LogoutFragment extends Fragment {
    private LoginButton mLogoutButton;
    private Button mLogout;

    public LogoutFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        mLogout = (Button) rootView.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootView;
    }
}
