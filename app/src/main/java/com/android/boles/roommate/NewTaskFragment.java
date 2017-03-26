package com.android.boles.roommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by boles on 3/22/2017.
 */

public class NewTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TASK_UPLOAD = "task_upload";
    public static final String TASK_CANCEL= "task_cancel";

    private EditText mTitle;
    private Spinner mSpinner;
    private String mForWho;
    private Button mUpload;
    private DatabaseReference mDatabase;
    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_task, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setHasOptionsMenu(true);
        getActivity().setTitle("New Task");
        mFragmentManager = getActivity().getSupportFragmentManager();
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView titleText = (TextView) rootView.findViewById(R.id.title_text);
        TextView whoText = (TextView) rootView.findViewById(R.id.for_who_text);

        mTitle = (EditText) rootView.findViewById(R.id.enter_title);
        mSpinner = (Spinner) rootView.findViewById(R.id.name_spinner);
        mUpload = (Button) rootView.findViewById(R.id.upload_task);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.names_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upload()) {
                    Fragment fragment = new MyTasksFragment();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mForWho = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(), mForWho, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(getApplicationContext(), "Please choose someone.", Toast.LENGTH_SHORT).show();
    }

    //upload new task to database
    private boolean upload() {

        if (TextUtils.isEmpty(mTitle.getText())) {
            Toast.makeText(getContext(), "Task must have a title!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else {
            String title = mTitle.getText().toString();
            Task task = new Task(title, mForWho, UUID.randomUUID());
            String id = task.getId();
            mDatabase.child("tasks").child(id).setValue(task);
            return true;
        }
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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
