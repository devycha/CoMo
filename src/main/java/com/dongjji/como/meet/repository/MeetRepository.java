package com.dongjji.como.meet.repository;

import com.dongjji.como.meet.entity.Meet;
import com.dongjji.como.meet.type.MeetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> findAllByEndDateBefore(LocalDate endDate);

    Page<Meet> findAllByStatus(MeetStatus status, Pageable pageable);

    List<Meet> findAllByEmail(String email);

    Optional<Meet> findByIdAndEmail(Long groupId, String email);
}
