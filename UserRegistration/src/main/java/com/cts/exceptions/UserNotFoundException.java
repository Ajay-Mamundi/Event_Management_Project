package com.cts.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String string) {
        super("User not found with id: " + string);
    }
}
