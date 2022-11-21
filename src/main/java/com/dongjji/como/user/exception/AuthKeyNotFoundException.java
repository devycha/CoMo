package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class AuthKeyNotFoundException extends RuntimeException {
    public AuthKeyNotFoundException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
