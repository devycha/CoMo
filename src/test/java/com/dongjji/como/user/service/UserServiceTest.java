package com.dongjji.como.user.service;

import com.dongjji.como.common.error.ErrorCode;
import com.dongjji.como.common.mail.GoogleMailSender;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.exception.AuthKeyNotFoundException;
import com.dongjji.como.user.exception.EmailAuthKeyExpiredException;
import com.dongjji.como.user.exception.LoginFailException;
import com.dongjji.como.user.exception.UserAlreadyExistException;
import com.dongjji.como.user.repository.UserRepository;
import com.dongjji.como.user.type.UserRole;
import com.dongjji.como.user.type.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private GoogleMailSender googleMailSender;

    @Test
    @DisplayName("회원 가입 실패 : 이미 존재하는 계정")
    void registerUserFailTest() {
        User alreadyExistUser = User.builder().email("already exist email").build();
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .username("already exist email").build();

        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(alreadyExistUser));

        // when
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
                () -> userService.register(registerUserDto));

        // then
        assertEquals(exception.getMessage(), "이미 존재하는 계정입니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 해당 계정 없음")
    void userLoginFailedByUserNotFoundTest() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("userEmail"));

        // then
        assertEquals(exception.getMessage(), ErrorCode.USERNAME_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 탈퇴한 계정")
    void userLoginFailedByDroppedTest() {
        // given
        User user = User.builder().status(UserStatus.DROP).build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        LoginFailException exception = assertThrows(LoginFailException.class,
                () -> userService.loadUserByUsername(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.UN_AVAILABLE_USER.getErrorMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 정지된 계정")
    void userLoginFailedByBannedTest() {
        // given
        User user = User.builder().status(UserStatus.BAN).build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        LoginFailException exception = assertThrows(LoginFailException.class,
                () -> userService.loadUserByUsername(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.UN_AVAILABLE_USER.getErrorMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 미인증")
    void userLoginFailedByUnAuthorizedEmailTest() {
        // given
        User user = User.builder().status(UserStatus.NEED_EMAIL_AUTH).build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        LoginFailException exception = assertThrows(LoginFailException.class,
                () -> userService.loadUserByUsername(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.UN_AUTHORIZED_EMAIL.getErrorMessage());
    }

    @Test
    @DisplayName("로그인 성공 - 관리자 계정")
    void userLoginSuccessWithAdminTest() {
//        // given
//        User user = User.builder()
//                .status(UserStatus.AVAILABLE).role(UserRole.ADMIN).build();
//        given(userRepository.findByEmail(anyString()))
//                .willReturn(Optional.of(user));
//
//        // when
//        userService.loadUserByUsername(anyString())



    }

    @Test
    @DisplayName("로그인 성공 - 일반 계정")
    void userLoginSuccessWithNormalTest() {

    }

    @Test
    @DisplayName("이메일 인증 실패 - 해당 이메일 인증키를 받은 유저 없음")
    void authorizeEmailFailedByAuthKeyNotFoundTest() {
        // given
        given(userRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.empty());

        // when
        AuthKeyNotFoundException exception = assertThrows(AuthKeyNotFoundException.class, () ->
                userService.authorizeEmail(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.AUTH_KEY_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 인증키의 유효기간이 만료됨")
    void authorizeEmailFailedByExpiredAuthKeyTest() {
        // given
        LocalDateTime validationDt = LocalDateTime.now().minusHours(1);
        User user = User.builder().emailAuthValidationDt(validationDt).build();

        given(userRepository.findByEmailAuthKey(anyString()))
                .willReturn(Optional.of(user));

        // when
        EmailAuthKeyExpiredException exception = assertThrows(EmailAuthKeyExpiredException.class, () ->
                userService.authorizeEmail(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.ALREADY_EXPIRED.getErrorMessage());
    }
}