package com.MTAPizza.Sympoll.groupmanagementservice.validator.exception;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.error.GeneralGroupError;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.error.ResourceNotFoundError;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.error.UserAlreadyMemberError;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.error.UserNotMemberError;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.found.ResourceNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserAlreadyMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserNotMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.request.RequestFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GroupExceptionHandler {

    /**
     * Handles resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ResourceNotFoundError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles user already a member exceptions
     */
    @ExceptionHandler(UserAlreadyMemberException.class)
    public ResponseEntity<UserAlreadyMemberError> handleUserAlreadyMemberException(UserAlreadyMemberException ex, WebRequest request) {
        return new ResponseEntity<>(new UserAlreadyMemberError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles user not a member exceptions
     */
    @ExceptionHandler(UserNotMemberException.class)
    public ResponseEntity<UserNotMemberError> handleUserNotMemberException(UserNotMemberException ex, WebRequest request) {
        return new ResponseEntity<>(new UserNotMemberError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles request failed exceptions
     */
    @ExceptionHandler(RequestFailedException.class)
    public ResponseEntity<ResourceNotFoundError> handleUserNotMemberException(RequestFailedException ex, WebRequest request) {
        return new ResponseEntity<>(new ResourceNotFoundError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralGroupError> handleUserNotMemberException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new GeneralGroupError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
