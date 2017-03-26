package com.android.boles.roommate;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by boles on 2/17/2017.
 */

public class Task {

    private UUID id;
    private String title;
    private String forWho;
    //private boolean isComplete;
    //private Date datePosted;
    //private Date dateCompleted;

    public Task() {

    }

    public Task(String title, String forWho, UUID id) {
        this.id = id;
        this.title = title;
        this.forWho = forWho;
        //datePosted = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getForWho() {
        return forWho;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setForWho(String forWho) {
        this.forWho = forWho;
    }
}
