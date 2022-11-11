package com.dongjji.como.meet.repository;

import com.dongjji.como.meet.entity.MeetInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetInvitationRepository extends JpaRepository<MeetInvitation, Long> {
    List<MeetInvitation> findAllByEmail(String email);
    boolean existsByMeetIdAndEmail(Long groupId, String email);
}
