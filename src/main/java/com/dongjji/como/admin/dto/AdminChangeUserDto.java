package com.dongjji.como.admin.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminChangeUserDto {
    private String gender;
    private boolean emailAuth;
    private String role;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
