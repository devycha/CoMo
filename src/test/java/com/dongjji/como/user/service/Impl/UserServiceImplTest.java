package com.dongjji.como.user.service.Impl;

import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.exception.UserAlreadyExistException;
import com.dongjji.como.user.repository.UserRepository;
import com.dongjji.como.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
}