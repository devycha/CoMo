package com.dongjji.como.user.exception;

import com.dongjji.como.common.error.ErrorCode;

public class SendEmailFailException extends RuntimeException {
    public SendEmailFailException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
