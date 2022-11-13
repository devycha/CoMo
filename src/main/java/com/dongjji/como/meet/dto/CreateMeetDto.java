package com.dongjji.como.meet.dto;

import com.dongjji.como.meet.entity.Meet;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class CreateMeetDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isPublic;

        private List<String> emails;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isPublic;
        private LocalDateTime createdAt;

        public static Response fromEntity(Meet meet) {
            return Response.builder()
                    .id(meet.getId())
                    .title(meet.getTitle())
                    .description(meet.getDescription())
                    .startDate(meet.getStartDate())
                    .endDate(meet.getEndDate())
                    .isPublic(meet.isPublic())
                    .createdAt(meet.getCreatedAt())
                    .build();
        }
    }
}
