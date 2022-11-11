package com.dongjji.como.meet.service;

import com.dongjji.como.meet.entity.Meet;
import com.dongjji.como.meet.entity.MeetApply;
import com.dongjji.como.meet.entity.MeetJoin;
import com.dongjji.como.meet.repository.MeetApplyRepository;
import com.dongjji.como.meet.repository.MeetJoinRepository;
import com.dongjji.como.meet.repository.MeetRepository;
import com.dongjji.como.meet.type.MeetJoinStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetApplyService {
    private final MeetRepository meetRepository;
    private final MeetApplyRepository meetApplyRepository;
    private final MeetJoinRepository meetJoinRepository;


    public List<MeetApply> getAllApplies(Long meetId, String email) {
        Meet meet = meetRepository.findByIdAndEmail(meetId, email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 그룹입니다."));

        return meetApplyRepository.findAllById(meet.getId());
    }

    public void applyMeetByMeetId(Long meetId, String email) {
        Meet meet = meetRepository.findById(meetId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 그룹입니다.")
        );

        MeetApply meetApply = MeetApply.builder()
                .meetId(meet.getId())
                .email(email)
                .status(MeetJoinStatus.PENDING)
                .build();

        meetApplyRepository.save(meetApply);
    }

    public Long refuseApply(Long applyId) {
        MeetApply apply = meetApplyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 참여 신청 입니다."));

        apply.setStatus(MeetJoinStatus.REFUSED);
        return apply.getMeetId();
    }

    public Long approveApply(Long applyId) {
        MeetApply apply = meetApplyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 참여 신청 입니다."));

        apply.setStatus(MeetJoinStatus.APPROVED);
        meetJoinRepository.save(
                MeetJoin.builder()
                        .meetId(apply.getMeetId())
                        .email(apply.getEmail())
                        .status(MeetJoinStatus.APPROVED)
                        .isJoined(true)
                        .build());
        return apply.getMeetId();
    }
}
