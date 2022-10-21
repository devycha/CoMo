package com.dongjji.como.user.service;

import com.dongjji.como.user.dto.RegisterUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void register(RegisterUserDto registerUserDto);
}
