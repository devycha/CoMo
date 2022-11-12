package com.dongjji.como.meet.service;

import com.dongjji.como.meet.dto.CreateMeetDto;
import com.dongjji.como.meet.dto.ExtendMeetPeriodDto;
import com.dongjji.como.meet.entity.Meet;
import com.dongjji.como.meet.repository.MeetApplyRepository;
import com.dongjji.como.meet.repository.MeetInvitationRepository;
import com.dongjji.como.meet.repository.MeetJoinRepository;
import com.dongjji.como.meet.repository.MeetRepository;
import com.dongjji.como.meet.type.MeetStatus;
import com.dongjji.como.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetService {
    private final MeetRepository meetRepository;

    public Page<Meet> getAllMeets(Pageable pageable) {
        return meetRepository.findAllByStatus(MeetStatus.ACTIVE, pageable);
    }

    public CreateMeetDto.Response createMeet(CreateMeetDto.Request createMeetDto) {
        Meet meet = meetRepository.save(Meet.fromEntity(createMeetDto));
        return CreateMeetDto.Response.fromEntity(meet);
    }



    public void deActivateMeet(Long meetId) {
        Meet meet = meetRepository.findById(meetId).orElseThrow(() ->
                new RuntimeException("존재하지 않는 그룹입니다"));

        meet.setStatus(MeetStatus.DISABLED);
        meetRepository.save(meet);
    }

    public void finishMeets() {
        List<Meet> expiredMeets = meetRepository.findAllByEndDateBefore(LocalDate.now());
        for (Meet meet : expiredMeets) {
            meet.setStatus(MeetStatus.FINISHED);
            meetRepository.save(meet);
        }
    }

    public void extendPeriod(ExtendMeetPeriodDto.Request extendMeetPeriodDto) {
        Meet meet = meetRepository.findById(extendMeetPeriodDto.getMeetId())
                .orElseThrow(() -> new RuntimeException("존재하지 않은 모임입니다."));

        if (extendMeetPeriodDto.getEndDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("종료 일자가 유효하지 않습니다.");
        }

        meet.setEndDate(extendMeetPeriodDto.getEndDate());
        meetRepository.save(meet);
    }
}