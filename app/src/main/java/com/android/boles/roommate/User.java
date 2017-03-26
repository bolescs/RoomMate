package com.android.boles.roommate;

import java.util.List;

/**
 * Created by boles on 2/17/2017.
 */

public class User {

    public static final String ANDREW = "Andrew";
    public static final String CAMERON = "Cameron";
    public static final String MATT = "Matt";
    public static final String MOE = "Moe";

    private String userName;
    private int Rating;
    private List<Task> tasksCompleted;
    private List<Task> pendingTasks;

    public User(String userName) {
        this.userName = userName;
    }

}
