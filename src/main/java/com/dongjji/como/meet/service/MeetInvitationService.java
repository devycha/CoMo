package com.dongjji.como.meet.service;

import com.dongjji.como.meet.dto.InviteMembersDto;
import com.dongjji.como.meet.entity.MeetInvitation;
import com.dongjji.como.meet.entity.MeetJoin;
import com.dongjji.como.meet.repository.MeetInvitationRepository;
import com.dongjji.como.meet.repository.MeetJoinRepository;
import com.dongjji.como.meet.type.MeetJoinStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetInvitationService {
    private final MeetInvitationRepository meetInvitationRepository;
    private final MeetJoinRepository meetJoinRepository;

    private void inviteMember(Long meetId, String email) {
        boolean exists = meetInvitationRepository
                .existsByMeetIdAndEmail(meetId, email);

        if (exists) {
            throw new RuntimeException("이미 초대완료 된 멤버입니다");
        }

        MeetInvitation meetInvitation = MeetInvitation.builder()
                .meetId(meetId)
                .email(email)
                .status(MeetJoinStatus.PENDING)
                .build();

        meetInvitationRepository.save(meetInvitation);
    }

    public void inviteMembers(InviteMembersDto.Request inviteMembersDto) {
        for (String email : inviteMembersDto.getEmailList()) {
            try {
                this.inviteMember(inviteMembersDto.getMeetId(), email);
            } catch (RuntimeException e) {
                log.info("Already Invited Member -> " + email);
            }
        }
    }

    public Long refuseInvitation(Long invitationId) {
        MeetInvitation invitation = meetInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 초대 요청 입니다."));

        invitation.setStatus(MeetJoinStatus.REFUSED);
        return invitation.getMeetId();
    }

    public Long approveInvitation(Long invitationId) {
        MeetInvitation invitation = meetInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 초대 요청 입니다."));

        invitation.setStatus(MeetJoinStatus.APPROVED);
        meetJoinRepository.save(
                MeetJoin.builder()
                        .meetId(invitation.getMeetId())
                        .email(invitation.getEmail())
                        .status(MeetJoinStatus.APPROVED)
                        .isJoined(true)
                        .build());


        return invitation.getMeetId();
    }

    public List<MeetInvitation> getAllInvitations(String email) {
        return meetInvitationRepository.findAllByEmail(email);
    }
}
