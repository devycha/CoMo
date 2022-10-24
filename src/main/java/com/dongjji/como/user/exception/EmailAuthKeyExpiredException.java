package com.dongjji.como.user.exception;

public class EmailAuthKeyExpiredException extends RuntimeException {
    public EmailAuthKeyExpiredException(String message) {
        super(message);
    }
}
