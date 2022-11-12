package com.dongjji.como.chat.exception;

import com.dongjji.como.common.error.ErrorCode;

public class ChatRoomNotFoundException extends RuntimeException {
    public ChatRoomNotFoundException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
