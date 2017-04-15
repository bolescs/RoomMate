package com.android.boles.roommate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by boles on 4/10/2017.
 */

public class UserProfileFragment extends Fragment {

    public static final String USER_NAME = "user_name";
    public static final String USER_PIC_ID = "user_pic_id";

    private TextView mName;
    private ProfilePictureView mPicture;
    //private double mRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mPicture = (ProfilePictureView) rootView.findViewById(R.id.profile_pic);
        mName = (TextView) rootView.findViewById(R.id.name_text_view);
        getActivity().setTitle(mName.getText() + "'s Profile");


        String name = getArguments().getString(USER_NAME);
        String pic = getArguments().getString(USER_PIC_ID);

        mPicture.setProfileId(pic);
        mName.setText(name);

        return rootView;
    }
}
