package com.MTAPizza.Sympoll.groupmanagementservice.exception.request;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String message) {
        super(message);
    }
}
