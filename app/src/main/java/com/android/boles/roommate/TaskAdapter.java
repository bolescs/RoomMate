package com.android.boles.roommate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by boles on 2/23/2017.
 */


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private ArrayList<Task> mTasks;
    private int lastPosition = -1;
    private static Context mContext;

    public TaskAdapter(ArrayList<Task> tasks, Context context) {
        mTasks = tasks;
        mContext = context;
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
        setAnimation(holder.itemView, position);
        holder.bindTask(itemTask);
    }

    @Override
    public void onViewDetachedFromWindow(TaskHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    //Inner viewHolder class.
    public static class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener
    {

        private RelativeLayout container;


        private TextView mTitle;
        private CheckBox mChooseTask;
        private RatingBar mTaskValue;
        private TextView mDatePosted;
        private ProfilePictureView mPic;
        private Task mTask;
        private static ArrayList<CheckBox> mAllCheckBoxes = new ArrayList<>();
        public static boolean isOneBoxChecked;
        public static ArrayList<Task> mDelete = new ArrayList<>();

        public TaskHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            container = (RelativeLayout) v.findViewById(R.id.recycler_item_container);
            mTitle = (TextView) v.findViewById(R.id.task_title);
            mChooseTask = (CheckBox) v.findViewById(R.id.checkbox_task);
            mTaskValue = (RatingBar) v.findViewById(R.id.value_display);
            mDatePosted = (TextView) v.findViewById(R.id.task_date);
            mPic = (ProfilePictureView) v.findViewById(R.id.task_author_pic);

            mAllCheckBoxes.add(mChooseTask);
            LayerDrawable stars = (LayerDrawable) mTaskValue.getProgressDrawable();
            stars.getDrawable(1).setTint(mContext.getResources().getColor(R.color.star_color));
            stars.getDrawable(2).setTint(mContext.getResources().getColor(R.color.star_color));
            mChooseTask.setChecked(false);

            mChooseTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isOneBoxChecked = true;
                        mDelete.add(mTask);
                    } else {
                        isOneBoxChecked = false;
                        mDelete.remove(mTask);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

            if (PublicTasksFragment.bigPushActive) {
                return;
            } else {
                FragmentManager fragmentManager =
                        ((FragmentActivity) mContext).getSupportFragmentManager();
                Fragment fragment = new TaskFragment();
                Bundle args = new Bundle();
                args.putString(TaskFragment.ARG_TASK_PIC_ID, mTask.getPicID());
                args.putString(TaskFragment.ARG_TASK_TITLE, mTask.getTitle());
                args.putString(TaskFragment.ARG_TASK_AUTHOR, mTask.getAuthor());
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment, "SINGLE_TASK_FRAGMENT")
                        .addToBackStack("singleTask")
                        .commit();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //displayCheckBoxes();
            return true;
        }

        //bind task
        public void bindTask(Task task) {
            mTask = task;
            mTitle.setText(mTask.getTitle());
            mTaskValue.setRating((float) mTask.getValue());
            mDatePosted.setText(mTask.getDatePosted());
            mPic.setProfileId(mTask.getPicID());
        }

        //make all checkboxes appear
        public static void displayCheckBoxes() {
            Animation translate = AnimationUtils.loadAnimation(mContext, R.anim.check_in);
            for (int i = 0; i < mAllCheckBoxes.size(); i++) {
                mAllCheckBoxes.get(i).setVisibility(View.VISIBLE);
                mAllCheckBoxes.get(i).startAnimation(translate);
            }
        }

        //hide all checkboxes
        public static void hideCheckBoxes() {
            Animation translate = AnimationUtils.loadAnimation(mContext, R.anim.check_out);
            for (int i = 0; i < mAllCheckBoxes.size(); i++) {
                mAllCheckBoxes.get(i).setVisibility(View.INVISIBLE);
                mAllCheckBoxes.get(i).startAnimation(translate);
            }
        }

        //uncheck if cancel clicked
        public static void unCheckBoxes() {
            for (int i = 0; i < mAllCheckBoxes.size(); i++) {
                mAllCheckBoxes.get(i).setChecked(false);
            }
        }

        //fix anim bug for fast scroll
        private void clearAnimation()
        {
            container.clearAnimation();
        }
    }
}
