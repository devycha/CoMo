package com.dongjji.como.common.error;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다"),
    UN_AUTHORIZED_EMAIL(HttpStatus.UNAUTHORIZED, "이메일 인증이 되지 않은 계정입니다."),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 계정입니다."),
    ALREADY_EXPIRED(HttpStatus.NOT_ACCEPTABLE, "유효 기간이 끝난 이메일 인증키입니다."),
    SEND_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    AUTH_KEY_NOT_FOUND(HttpStatus.BAD_REQUEST, "발급되지 않거나 만료된 이메일 인증 키입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    UN_AVAILABLE_USER(HttpStatus.UNAUTHORIZED, "사용할 수 없는 계정입니다.");


    private final HttpStatus errorCode;
    private final String errorMessage;
}
