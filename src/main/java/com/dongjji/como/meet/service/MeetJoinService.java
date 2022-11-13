package com.dongjji.como.meet.service;

import com.dongjji.como.meet.dto.MeetMembersDto;
import com.dongjji.como.meet.repository.MeetJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetJoinService {
    private final MeetJoinRepository meetJoinRepository;

    public MeetMembersDto.Response getMeetMembers(Long meetId) {
        return MeetMembersDto.Response.fromEntity(
                meetJoinRepository.findAllByMeetId(meetId)
        );
    }
}
