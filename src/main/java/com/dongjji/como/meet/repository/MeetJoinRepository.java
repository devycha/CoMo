package com.dongjji.como.meet.repository;

import com.dongjji.como.meet.entity.MeetJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetJoinRepository extends JpaRepository<MeetJoin, Long> {
    List<MeetJoin> findAllByMeetId(Long meetId);
}
