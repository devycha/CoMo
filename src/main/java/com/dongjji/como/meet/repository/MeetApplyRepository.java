package com.dongjji.como.meet.repository;

import com.dongjji.como.meet.entity.MeetApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetApplyRepository extends JpaRepository<MeetApply, Long> {
    List<MeetApply> findAllById(Long id);
}
