package com.android.boles.roommate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

/**
 * Created by boles on 3/22/2017.
 */

public class NewTaskFragment extends Fragment {

    private EditText mTitle;
    private int mValue;
    //private Spinner mSpinner;
    //private String mForWho;
    private ImageButton mUpload;
    private RatingBar mRatingBar;
    private DatabaseReference mDatabase;
    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_task, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((DrawerLock) getActivity()).setDrawerEnabled(false);
        getActivity().setTitle("New Task");
        mFragmentManager = getActivity().getSupportFragmentManager();

        TextView titleText = (TextView) rootView.findViewById(R.id.title_text);

        mTitle = (EditText) rootView.findViewById(R.id.enter_title);
        mRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(1).setTint(getResources().getColor(R.color.star_color));
        stars.getDrawable(2).setTint(getResources().getColor(R.color.star_color));

        //mSpinner = (Spinner) rootView.findViewById(R.id.name_spinner);
        mUpload = (ImageButton) rootView.findViewById(R.id.upload_task);

        mTitle.requestFocus();
        showKeyboard();

        /**
         *
        Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.names_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
         */

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upload()) {
                    Fragment fragment = new PublicTasksFragment();
                    mFragmentManager.beginTransaction()
                            //.setCustomAnimations(R.anim.frag_gone, R.anim.slide_out)
                            .replace(R.id.content_frame, fragment, "TASKS_FRAGMENT")
                            .addToBackStack("myTaskFrag2")
                            .commit();

                }
            }
        });


        return rootView;
    }

    /**
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mForWho = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(), mForWho, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(getApplicationContext(), "Please choose someone.", Toast.LENGTH_SHORT).show();
    }
    */

    //upload new task to database
    private boolean upload() {

        if (TextUtils.isEmpty(mTitle.getText())) {
            Toast.makeText(getContext(), "Task must have a title!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else {
            String title = mTitle.getText().toString();
            Date date = new Date();
            String datePosted = (DateFormat.format("EEE MMM d, yyyy", date)).toString();
            Task task = new Task(title, MainActivity.mFacebookName, mRatingBar.getRating(),
                    UUID.randomUUID().toString(),
                    MainActivity.mPictureID, datePosted, System.currentTimeMillis());
            mDatabase.child("tasks").child(task.getId()).setValue(task);

            User user = new User(UUID.randomUUID().toString(), MainActivity.mFacebookName,
                    MainActivity.mPictureID);
            mDatabase.child("users").child(user.getUserId()).setValue(user);
            return true;
        }
    }

    //automatically focus EditText
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //hide keyboard when navigating away from fragment
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_task_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.cancel_new_task:
                mFragmentManager.popBackStackImmediate();
                closeKeyboard(getActivity(), mTitle.getWindowToken());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard(getActivity(), mTitle.getWindowToken());
    }
}
