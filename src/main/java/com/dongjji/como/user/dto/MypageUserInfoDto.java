package com.dongjji.como.user.dto;

import com.dongjji.como.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageUserInfoDto {
    private String id;
    private String email;
    private LocalDate birth;
    private String gender;

    public static MypageUserInfoDto of(User user) {
        return MypageUserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .birth(user.getBirth())
                .build();
    }
}
