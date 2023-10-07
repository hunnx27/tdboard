package com.twodollar.tdboard.modules.admin.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 사용자명
    private String username;

    // 비밀번호
    private String password;

    // 이메일
    private String email;

    // 권한
    private String role;

    // 계정 생성일
    @CreationTimestamp
    private LocalDateTime createdDate;
}
