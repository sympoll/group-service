package com.MTAPizza.Sympoll.groupmanagementservice.exception.group;

import java.util.UUID;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String groupId) {
        super("Group with ID '" + groupId + "' was not found");
    }
}
