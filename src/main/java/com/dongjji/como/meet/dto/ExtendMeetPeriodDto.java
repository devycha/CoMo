package com.dongjji.como.meet.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ExtendMeetPeriodDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long meetId;
        private LocalDate endDate;
    }
}
