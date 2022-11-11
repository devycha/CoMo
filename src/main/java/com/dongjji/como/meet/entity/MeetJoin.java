package com.dongjji.como.meet.entity;

import com.dongjji.como.meet.type.MeetJoinStatus;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Meet_Join")
@EntityListeners(AuditingEntityListener.class)
public class MeetJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetId;
    private String email;
    private boolean isJoined;
    private MeetJoinStatus status;
}
