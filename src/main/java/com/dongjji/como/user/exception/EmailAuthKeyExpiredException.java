package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class EmailAuthKeyExpiredException extends RuntimeException {
    public EmailAuthKeyExpiredException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
