package com.dongjji.como.meet.entity;

import com.dongjji.como.meet.dto.CreateMeetDto;
import com.dongjji.como.meet.type.MeetStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Meet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String title;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean isPublic;

    @Enumerated(EnumType.STRING)
    private MeetStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Meet fromEntity(CreateMeetDto.Request createMeetDto) {
        return Meet.builder()
                .title(createMeetDto.getTitle())
                .description(createMeetDto.getDescription())
                .startDate(createMeetDto.getStartDate())
                .endDate(createMeetDto.getEndDate())
                .isPublic(createMeetDto.isPublic())
                .status(MeetStatus.ACTIVE)
                .build();
    }
}
