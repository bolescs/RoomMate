package com.android.boles.roommate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by boles on 4/8/2017.
 */

public class TaskFragment extends Fragment {

    public static final String ARG_TASK_TITLE = "task_title";
    public static final String ARG_TASK_PIC_ID = "pic_id";
    public static final String ARG_TASK_AUTHOR = "author";

    private Task mTask;
    private FragmentManager mFragmentManager;
    private TextView mTitle;
    private ProfilePictureView mPic;
    private String arg_title;
    private String arg_picId;
    private String arg_author;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_task, container, false);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((DrawerLock) getActivity()).setDrawerEnabled(false);
        mFragmentManager = getActivity().getSupportFragmentManager();

        mTitle = (TextView) v.findViewById(R.id.single_task_title);
        mPic = (ProfilePictureView) v.findViewById(R.id.single_task_author_pic);

        arg_picId = getArguments().getString(ARG_TASK_PIC_ID);
        arg_title = getArguments().getString(ARG_TASK_TITLE);
        arg_author = getArguments().getString(ARG_TASK_AUTHOR);

        mTitle.setText(arg_title);
        mPic.setProfileId(arg_picId);

        mPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arg_picId.equals(MainActivity.mPictureID)) {
                    loadMyProfile();
                } else {
                    loadUserProfile();
                }
            }
        });
        return v;
    }

    //if it's your task, load your profile
    public void loadMyProfile() {
        Fragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(MyProfileFragment.ARG_NAME, MainActivity.mFacebookName);
        args.putString(MyProfileFragment.ARG_PIC_ID, arg_picId);
        fragment.setArguments(args);
        mFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, "PROFILE_FRAGMENT")
                .addToBackStack("profileFrag")
                .commit();
    }

    //if it's someone else's task, load their profile
    public void loadUserProfile() {
        Fragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(UserProfileFragment.USER_NAME, arg_author);
        args.putString(UserProfileFragment.USER_PIC_ID, arg_picId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.drawable.ic_action_logout:
                mFragmentManager.popBackStackImmediate();
                break;
        }
        return true;
    }

}
