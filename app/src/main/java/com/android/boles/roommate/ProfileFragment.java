package com.android.boles.roommate;

/**
 * Created by boles on 2/13/2017.
 */

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

/**
 * Fragment that appears in the "content_frame", shows profile
 */
public class ProfileFragment extends Fragment{

    private ProfilePictureView mProfilePic;
    private TextView mName;

    public ProfileFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mProfilePic = (ProfilePictureView) rootView.findViewById(R.id.profile_pic);
        mName = (TextView) rootView.findViewById(R.id.name_text_view);


        String name = getArguments().getString("name");
        String pic = getArguments().getString("picID");

        mProfilePic.setProfileId(pic);
        mName.setText(name);

        return rootView;
    }
}
