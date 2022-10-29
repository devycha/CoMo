package com.dongjji.como.user.service;

import com.dongjji.como.common.error.ErrorCode;
import com.dongjji.como.common.mail.GoogleMailSender;
import com.dongjji.como.user.dto.ChangeUserInfoDto;
import com.dongjji.como.user.dto.MypageUserInfoDto;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.exception.*;
import com.dongjji.como.user.repository.UserRepository;
import com.dongjji.como.user.type.Gender;
import com.dongjji.como.user.type.UserRole;
import com.dongjji.como.user.type.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        // given
        User user = User.builder()
                .status(UserStatus.AVAILABLE).role(UserRole.ADMIN).build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        UserDetails userDetails = userService.loadUserByUsername(anyString());

        // then
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        assertEquals(userDetails.getAuthorities().size(), 2);
        assertArrayEquals(userDetails.getAuthorities().toArray(), grantedAuthorities.toArray());
    }

    @Test
    @DisplayName("로그인 성공 - 일반 계정")
    void userLoginSuccessWithNormalTest() {
        // given
        User user = User.builder()
                .status(UserStatus.AVAILABLE).role(UserRole.USER).build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        UserDetails userDetails = userService.loadUserByUsername(anyString());

        // then
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        assertEquals(userDetails.getAuthorities().size(), 1);
        assertArrayEquals(userDetails.getAuthorities().toArray(), grantedAuthorities.toArray());
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

    @Test
    @DisplayName("마이페이지 열람 실패 - 존재하지 않는 계정")
    void getMypageFailedByUserNotFoundTest() {
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserInfoByEmail(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.USERNAME_NOT_FOUND.getErrorMessage());

    }

    @Test
    @DisplayName("마이페이지 열람 성공")
    void getMyPageSuccessTest() {
        // given
        LocalDate date = LocalDate.now();
        User user = User.builder().id("1").email("email").birth(date).gender(Gender.MEN)
                        .build();
        MypageUserInfoDto userInfo = MypageUserInfoDto.of(user);

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        // when
        MypageUserInfoDto userInfoDto = userService.getUserInfoByEmail(anyString());

        // then
        assertEquals(userInfoDto.getId(), userInfo.getId());
        assertEquals(userInfoDto.getEmail(), userInfo.getEmail());
        assertEquals(userInfoDto.getBirth(), userInfo.getBirth());
        assertEquals(userInfoDto.getGender(), userInfo.getGender());
    }

    @Test
    @DisplayName("유저 정보 변경 실패 - 존재하지 않는 계정")
    void changeUserInfoFailedByUserNotFoundTest() {
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserInfoByEmail(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.USERNAME_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("유저 정보 변경 실패 - 본인 계정 아님(권한 없음)")
    void changeUserInfoFailedByUserNotSame() {
        // given
        String userId = "1";
        String email = "email";
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).email(email).birth(date).gender(Gender.MEN)
                .build();
        String currentUserEmail = "not same email";
        ChangeUserInfoDto changeUserInfoDto = ChangeUserInfoDto.builder()
                .email(currentUserEmail)
                .gender(Gender.MEN.toString())
                .birth(date).build();


        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));

        // when
        ChangeUserInfoFailedException exception = assertThrows(ChangeUserInfoFailedException.class,
                () -> userService.changeUserInfo(userId, currentUserEmail, changeUserInfoDto));

        // then
        assertEquals(exception.getMessage(), ErrorCode.FORBIDDEN.getErrorMessage());
    }

    @Test
    @DisplayName("유저 정보 변경 성공")
    void changeUserInfoSuccessTest() {
        // given
        String userId = "1";
        String email = "email";
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).email(email).birth(date.minusDays(1)).gender(Gender.WOMEN)
                .build();

        ChangeUserInfoDto changeUserInfoDto = ChangeUserInfoDto.builder()
                .email(email)
                .gender(Gender.MEN.toString())
                .birth(date).build();


        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));

        // when
        MypageUserInfoDto mypageUserInfoDto =
                userService.changeUserInfo(userId, email, changeUserInfoDto);

        // then
        assertEquals(user.getBirth(), date);
        assertEquals(user.getGender(), Gender.MEN);
    }

    @Test
    @DisplayName("계정 탈퇴 실패 - 존재하지 않는 계정")
    void deleteUserFailedByUserNotFoundTest() {
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserInfoByEmail(anyString()));

        // then
        assertEquals(exception.getMessage(), ErrorCode.USERNAME_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("계정 탈퇴 실패 - 본인 계정 아님(권한 없음)")
    void deleteUserFailedByUserNotSameTest() {
        String userId = "1";
        String email = "email";
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).email(email).birth(date).gender(Gender.MEN)
                .build();
        String currentUserEmail = "not same email";
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));

        // when
        ChangeUserInfoFailedException exception = assertThrows(ChangeUserInfoFailedException.class,
                () -> userService.dropUser(userId, currentUserEmail));

        // then
        assertEquals(exception.getMessage(), ErrorCode.FORBIDDEN.getErrorMessage());
    }

}