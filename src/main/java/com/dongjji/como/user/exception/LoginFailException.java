package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class LoginFailException extends RuntimeException {
    public LoginFailException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
