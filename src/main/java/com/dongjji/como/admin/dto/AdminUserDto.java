package com.dongjji.como.admin.dto;

import com.dongjji.como.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDto {
    private String id;
    private String email;
    private LocalDate birth;
    private String provider;
    private String gender;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean emailAuth;
    private String role;
    private String status;

    public static AdminUserDto of(User user) {
        return AdminUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .birth(user.getBirth())
                .gender(user.getGender().toString())
                .provider(user.getProvider())
                .emailAuth(user.isEmailAuth())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .createdAt(user.getCreatedAt().toLocalDate())
                .updatedAt(user.getUpdatedAt().toLocalDate())
                .build();
    }
}
