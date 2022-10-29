package com.dongjji.como.user.entity;

import com.dongjji.como.user.type.Gender;
import com.dongjji.como.user.type.UserRole;
import com.dongjji.como.user.type.UserStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="id", unique = true)
    private String id;

    @Column(unique = true)
    private String email;

    private String password;
    private LocalDate birth;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean emailAuth;
    private String emailAuthKey;
    private LocalDateTime emailAuthValidationDt;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
