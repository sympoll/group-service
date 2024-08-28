package com.MTAPizza.Sympoll.groupmanagementservice.exception.found;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
