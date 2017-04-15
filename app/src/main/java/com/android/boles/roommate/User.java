package com.android.boles.roommate;

import java.util.List;
import java.util.UUID;

/**
 * Created by boles on 2/17/2017.
 */

public class User {

    private String userId;
    private String name;
    private String picID;
    //private double rating;
    //private List<Task> tasksCompleted;
    //private List<Task> pendingTasks;

    public User() {

    }

    public User(String userId, String name, String picID) {
        this.userId = userId;
        this.name = name;
        this.picID = picID;
        //this.rating = rating;
        //this.tasksCompleted = tasksCompleted;
        //this.pendingTasks = pendingTasks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicID() {
        return picID;
    }

    public void setPicID(String picID) {
        this.picID = picID;
    }

    /**
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Task> getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(List<Task> tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public List<Task> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<Task> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }
     */
}
