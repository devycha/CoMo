package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class ChangeUserInfoFailedException extends RuntimeException {
    public ChangeUserInfoFailedException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
