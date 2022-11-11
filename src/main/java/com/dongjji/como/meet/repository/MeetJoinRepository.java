package com.dongjji.como.meet.repository;

import com.dongjji.como.meet.entity.MeetJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetJoinRepository extends JpaRepository<MeetJoin, Long> {
    Optional<MeetJoin> findByMeetIdAndEmail(Long groupId, String email);
    boolean existsByMeetIdAndEmail(Long groupId, String email);
}
