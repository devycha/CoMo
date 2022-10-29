package com.dongjji.como.user.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserInfoDto {
    private String email;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
