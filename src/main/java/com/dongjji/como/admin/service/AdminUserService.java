package com.dongjji.como.admin.service;

import com.dongjji.como.admin.dto.AdminChangeUserDto;
import com.dongjji.como.admin.dto.AdminUserDto;
import com.dongjji.como.admin.dto.AdminUserListDto;
import com.dongjji.como.common.error.ErrorCode;
import com.dongjji.como.user.entity.User;
import com.dongjji.como.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {
    private final UserRepository userRepository;

    public List<AdminUserListDto> getAllUsers() {
        List<AdminUserListDto> userList = new ArrayList<>();

        userRepository.findAll().forEach(user -> {
            userList.add(
                    AdminUserListDto.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .emailAuth(user.isEmailAuth())
                            .status(user.getStatus().toString()).build()
            );
        });

        return userList;
    }

    public AdminUserDto getUserById(String userId) {
        return AdminUserDto.of(
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(ErrorCode.USERNAME_NOT_FOUND.getErrorMessage())));
    }

    public AdminUserDto changeUserInfo(String userId, AdminChangeUserDto adminChangeUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        ErrorCode.USERNAME_NOT_FOUND.getErrorMessage()));


        user.changeUserByAdmin(adminChangeUserDto);
        return AdminUserDto.of(user);
    }
}
;