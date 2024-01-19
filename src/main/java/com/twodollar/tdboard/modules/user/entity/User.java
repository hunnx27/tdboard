package com.twodollar.tdboard.modules.user.entity;

import com.twodollar.tdboard.modules.auth.controller.request.UserAuthRequest;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.controller.response.UserResponse;
import com.twodollar.tdboard.modules.user.entity.enums.RoleEnum;
import com.twodollar.tdboard.modules.user.entity.enums.SexEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private SexEnum sex;
    private String email;
    private String phone;
    private String birthday;
    private String channel; //유입채널
    @Enumerated(EnumType.STRING)
    private RoleEnum role; // 권한
    @CreationTimestamp
    private LocalDateTime createdDate;
    private String refreshToken;

    public UserResponse toResponse() {
        return UserResponse.builder()
                .id(this.id)
                .userId(this.userId)
                .username(this.username)
                .sex(this.sex.name())
                .email(this.email)
                .phone(this.phone)
                .birthday(this.birthday)
                .channel(this.channel)
                .role(this.role.name())
                .createdDate(this.createdDate)
                .build();
    }

    public void update(UserRequest userRequest) {
        this.username = userRequest.getUsername();
        this.sex = SexEnum.valueOf(userRequest.getSex());
        this.email = userRequest.getEmail();
        this.phone = userRequest.getPhone();
        this.birthday = userRequest.getBirthday();
        this.channel = userRequest.getChannel();
        this.role = RoleEnum.valueOf(userRequest.getRole());
    }
}
