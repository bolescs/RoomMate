package com.android.boles.roommate;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;


/**
 * Created by boles on 2/14/2017.
 */

public class PublicTasksFragment extends Fragment {

    private TextView mEmptyTask;
    private ProgressBar mProgressBar;
    private RecyclerView mTasksRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TaskAdapter mAdapter;
    private ArrayList<Task> mTasks;
    private ImageButton mNewTask;
    private Button mSubmit;
    public static boolean bigPushActive;
    private MenuItem mCancel;
    private MenuItem mCheck;
    public static boolean taskAvailable;
    private FragmentManager mFragmentManager;
    private DatabaseReference mDatabase;



    public PublicTasksFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        setHasOptionsMenu(true);
        bigPushActive = false;
        mFragmentManager = getActivity().getSupportFragmentManager();

        //mEmptyTask = (TextView) rootView.findViewById(R.id.empty_task_view);
        mNewTask = (ImageButton) rootView.findViewById(R.id.new_task);
        mSubmit = (Button) rootView.findViewById(R.id.submit_button);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mTasksRecyclerView = (RecyclerView) rootView.findViewById(R.id.task_recycler_view);
        mTasksRecyclerView.setLayoutManager(mLayoutManager);

        mTasks = new ArrayList<>();

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.tasks_loading);
        firebase();


        //new task button
        mNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newTaskFragment = new NewTaskFragment();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.frag_gone,
                                R.anim.frag_gone, R.anim.frag_gone)
                        .replace(R.id.content_frame, newTaskFragment, "TASKS_FRAGMENT")
                        .addToBackStack("newTask")
                        .commit();
            }
        });

        //submit completed tasks
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TaskAdapter.TaskHolder.isOneBoxChecked) {
                    ArrayList<Task> tasks = TaskAdapter.TaskHolder.mDelete;
                    for (int i = 0; i < tasks.size(); i++) {
                        String id = tasks.get(i).getId();
                        mDatabase.child("tasks").child(id).removeValue();
                        //Toast.makeText(getContext(), id, Toast.LENGTH_LONG).show();
                    }

                    Fragment fragment = mFragmentManager.findFragmentByTag("TASKS_FRAGMENT");
                    mFragmentManager.beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();

                    TaskAdapter.TaskHolder.isOneBoxChecked = false;
                }
            }
        });

        return rootView;
    }

    public void firebase() {

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getTasks(dataSnapshot);
                mTasksRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getTasks(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //populate recyclerview with data from firebase
    private void getTasks(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

            /**
            String taskTitle = singleSnapshot.child("title").getValue(String.class);
            String taskAuthor = singleSnapshot.child("author").getValue(String.class);
            double taskValue = singleSnapshot.child("value").getValue(Double.class);
            String datePosted = singleSnapshot.child("datePosted").getValue(String.class);
            String id = singleSnapshot.child("id").getValue(String.class);
            String picID = singleSnapshot.child("picID").getValue(String.class);
            long timeStamp = singleSnapshot.child("timeStamp").getValue(Long.class);
            mTasks.add(new Task(taskTitle, taskAuthor, taskValue, id, picID, datePosted, timeStamp));
            */
            mTasks.add(singleSnapshot.getValue(Task.class));
        }
        Collections.sort(mTasks);
        mAdapter = new TaskAdapter(mTasks, getContext());
        mTasksRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task, menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCheck = menu.findItem(R.id.action_big_push);
        mCancel = menu.findItem(R.id.action_cancel);
        mCancel.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Animation submitUp = AnimationUtils.loadAnimation(getContext(), R.anim.submit_in);
        Animation submitDown = AnimationUtils.loadAnimation(getContext(), R.anim.submit_out);
        View v = getView();

        switch (item.getItemId()) {
            case R.id.action_big_push:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                item.setVisible(false);
                mCancel.setVisible(true);
                bigPushActive = true;
                mNewTask.setVisibility(View.INVISIBLE);
                mNewTask.startAnimation(submitDown);
                mSubmit.setVisibility(View.VISIBLE);
                mSubmit.startAnimation(submitUp);
                TaskAdapter.TaskHolder.displayCheckBoxes();
                return true;

            case R.id.action_cancel:
                item.setVisible(false);
                mCheck.setVisible(true);
                bigPushActive = false;
                mNewTask.setVisibility(View.VISIBLE);
                mNewTask.startAnimation(submitUp);
                mSubmit.setVisibility(View.INVISIBLE);
                mSubmit.startAnimation(submitDown);
                TaskAdapter.TaskHolder.hideCheckBoxes();
                TaskAdapter.TaskHolder.unCheckBoxes();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(ProfileActivity.TASK_HEADER);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((DrawerLock) getActivity()).setDrawerEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
