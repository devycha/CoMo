package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
