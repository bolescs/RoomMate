package com.android.boles.roommate;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by boles on 2/17/2017.
 */

public class Task implements Comparable<Task> {

    private String id;
    private String title;
    private String author;
    private double value;
    private String picID;
    private String datePosted;
    private long timeStamp;
    //private boolean isComplete;
    //private Date datePosted;
    //private Date dateCompleted;

    public Task() {

    }

    public Task(String title, String author, double value, String id, String picID, String datePosted, long timeStamp) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.value = value;
        this.picID = picID;
        this.datePosted = datePosted;
        this.timeStamp = timeStamp;
        //datePosted = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getValue() {
        return value;
    }

    public String getPicID() {
        return picID;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setPicID(String picID) {
        this.picID = picID;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(@NonNull Task task) {
        long time = ((Task) task).getTimeStamp();
        if (this.timeStamp > time) {
            return -1;
        } else if (this.timeStamp < time) {
            return 1;
        }
        return 0;
    }
}
