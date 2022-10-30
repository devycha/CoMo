package com.dongjji.como.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserListDto {
    private String id;
    private String email;
    private boolean emailAuth;
    private String status;
}
