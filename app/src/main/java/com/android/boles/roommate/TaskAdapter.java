package com.android.boles.roommate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by boles on 2/23/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private ArrayList<Task> mTasks;

    public TaskAdapter(ArrayList<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_list_item, parent, false);
        return new TaskHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.TaskHolder holder, int position) {
        Task itemTask = mTasks.get(position);
        holder.bindTask(itemTask);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private CheckBox mChooseTask;
        private TextView mForWho;
        private Task mTask;
        public static ArrayList<Task> mDelete = new ArrayList<>();

        public TaskHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.task_title);
            mChooseTask = (CheckBox) v.findViewById(R.id.checkbox_task);
            mForWho = (TextView) v.findViewById(R.id.task_for_who);

            mChooseTask.setChecked(false);

            if (MyTasksFragment.bigPushActive) {
                mChooseTask.setVisibility(View.VISIBLE);
            }

            mChooseTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDelete.add(mTask);
                }
            });
        }

        public void bindTask(Task task) {
            mTask = task;
            mTitle.setText(mTask.getTitle());
            mForWho.setText(mTask.getForWho());
        }
    }
}
