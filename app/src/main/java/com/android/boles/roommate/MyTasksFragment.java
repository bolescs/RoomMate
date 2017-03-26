package com.android.boles.roommate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.jar.JarInputStream;

import static android.content.ContentValues.TAG;


/**
 * Created by boles on 2/14/2017.
 */

public class MyTasksFragment extends Fragment {



    private TextView mEmptyTask;
    private RecyclerView mTasksRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TaskAdapter mAdapter;
    private ArrayList<Task> mTasks;
    private ImageButton mNewTask;
    private Button mSubmit;
    public static boolean bigPushActive;
    private MenuItem mCancel;
    private MenuItem mCheck;
    private FragmentManager mFragmentManager;

    private DatabaseReference mDatabase;
    private DatabaseReference mRef;



    public MyTasksFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        setHasOptionsMenu(true);
        bigPushActive = false;
        mFragmentManager = getActivity().getSupportFragmentManager();

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //mEmptyTask = (TextView) rootView.findViewById(R.id.empty_task_view);
        mNewTask = (ImageButton) rootView.findViewById(R.id.new_task);
        mSubmit = (Button) rootView.findViewById(R.id.submit_button);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mTasksRecyclerView = (RecyclerView) rootView.findViewById(R.id.task_recycler_view);
        mTasksRecyclerView.setLayoutManager(mLayoutManager);

        mTasks = new ArrayList<>();

        final ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getTasks(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getTasks(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //deleteTasks(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addChildEventListener(childListener);

        //new task button
        mNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newTaskFragment = new NewTaskFragment();
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.frag_gone,
                                R.anim.frag_gone, R.anim.slide_out)
                        .replace(R.id.content_frame, newTaskFragment)
                        .addToBackStack("newTask")
                        .commit();
            }
        });

        //submit completed tasks
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        return rootView;
    }

    //populate recyclerview with data from firebase
    private void getTasks(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

            String taskTitle = singleSnapshot.child("title").getValue(String.class);
            String forWho = singleSnapshot.child("forWho").getValue(String.class);
            String id = singleSnapshot.child("id").getValue(String.class);
            mTasks.add(new Task(taskTitle, forWho, UUID.fromString(id)));
        }

        mAdapter = new TaskAdapter(mTasks);
        mTasksRecyclerView.setAdapter(mAdapter);

    }

    //delete data from recyclerview
    public void deleteTasks(DataSnapshot dataSnapshot) {

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.child("title").getValue(String.class);
            for(int i = 0; i < mTasks.size(); i++) {
                if(mTasks.get(i).getTitle().equals(taskTitle)) {
                    mTasks.remove(i);
                }
            }
            Log.d(TAG, "Task tile " + taskTitle);
            mAdapter.notifyDataSetChanged();
            mAdapter = new TaskAdapter(mTasks);
            mTasksRecyclerView.setAdapter(mAdapter);
        }

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

        switch (item.getItemId()) {
            case R.id.action_big_push:
                item.setVisible(false);
                mCancel.setVisible(true);
                bigPushActive = true;
                mNewTask.setVisibility(View.INVISIBLE);
                mSubmit.setVisibility(View.VISIBLE);
                mAdapter = new TaskAdapter(mTasks);
                mTasksRecyclerView.setAdapter(mAdapter);
                return true;

            case R.id.action_cancel:
                item.setVisible(false);
                mCheck.setVisible(true);
                bigPushActive = false;
                mNewTask.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.INVISIBLE);
                mAdapter = new TaskAdapter(mTasks);
                mTasksRecyclerView.setAdapter(mAdapter);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Tasks");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
