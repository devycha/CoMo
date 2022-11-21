package com.dongjji.como.chat.exception;

import com.dongjji.como.common.error.ErrorCode;

public class UnAuthorizedChatRoomAccessException extends RuntimeException {
    public UnAuthorizedChatRoomAccessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
