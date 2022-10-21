package com.dongjji.como.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterUserDto {
    private String username;
    private String password;
    private String gender;
    private String birth;
}
