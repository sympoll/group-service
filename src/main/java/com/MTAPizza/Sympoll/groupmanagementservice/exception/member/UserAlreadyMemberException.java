package com.MTAPizza.Sympoll.groupmanagementservice.exception.member;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}
