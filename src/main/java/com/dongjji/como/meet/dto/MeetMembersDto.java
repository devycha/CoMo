package com.dongjji.como.meet.dto;

import com.dongjji.como.meet.entity.MeetJoin;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MeetMembersDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private List<String> emails;

        public static Response fromEntity(List<MeetJoin> members) {
            return Response.builder()
                    .emails(
                        members.stream()
                            .filter(MeetJoin::isJoined)
                                .map(MeetJoin::getEmail)
                            .collect(Collectors.toList())
                    ).build();
        }
    }
}
