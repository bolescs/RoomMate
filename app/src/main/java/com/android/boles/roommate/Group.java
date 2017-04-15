package com.android.boles.roommate;

import java.util.List;
import java.util.UUID;

/**
 * Created by boles on 4/10/2017.
 */

public class Group {

    private UUID groupId;
    private String groupName;
    private List<User> members;

    public Group(UUID groupId, String groupName, List<User> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.members = members;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
