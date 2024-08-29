package com.MTAPizza.Sympoll.groupmanagementservice.exception.member;

public class UserNotMemberException extends RuntimeException {
    public UserNotMemberException(String message) {
        super(message);
    }
}
