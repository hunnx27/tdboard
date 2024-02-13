package com.twodollar.tdboard.modules.user.entity;

import com.twodollar.tdboard.modules.auth.controller.request.UserAuthRequest;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.controller.response.UserResponse;
import com.twodollar.tdboard.modules.user.entity.enums.ChannelEnum;
import com.twodollar.tdboard.modules.user.entity.enums.RoleEnum;
import com.twodollar.tdboard.modules.user.entity.enums.SexEnum;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "comment 'F:여성,M:남성' ")
    private SexEnum sex;
    private String email;
    private String phone;
    private String birthday;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "comment 'C01: 팜플렛(카테고리),C02: TV광고,C03: 인터넷광고,C04: 엘리베이터 광고,C05: 인터넷 검색,C06: 신문,C07: 현수막,C08: 지인소개,C09: 기타' ")
    private ChannelEnum channel; //유입채널
    @ColumnDefault("'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "comment 'ROLE_USER:회원, ROLE_ADMIN:관리자, ROLE_PROFESSOR:교수, ROLE_ORG:기관' ")
    private RoleEnum role; // 권한
    @ColumnDefault("'N'")
    private String delYn;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;// 수정일
    private String refreshToken;

    public UserResponse toResponse() {
        return UserResponse.builder()
                .id(this.id)
                .userId(this.userId)
                .username(this.username)
                .sex(this.sex!=null?this.sex.name():null)
                .email(this.email)
                .phone(this.phone)
                .birthday(this.birthday)
                .channel(this.channel!=null?this.channel.name():null)
                .role(this.role.name())
                .createdAt(this.createdAt)
                .build();
    }

    public void update(UserRequest userRequest) {
        this.username = userRequest.getUsername();
        this.sex = userRequest.getSex()!=null ? SexEnum.valueOf(userRequest.getSex()) : this.sex;
        this.email = userRequest.getEmail();
        this.phone = userRequest.getPhone();
        this.birthday = userRequest.getBirthday();
        this.channel = userRequest.getChannel()!=null ? ChannelEnum.valueOf(userRequest.getChannel()) : this.channel;
//        this.role = RoleEnum.valueOf(userRequest.getRole());
    }

    public void memberUpdate(UserRequest userRequest){
        this.email = userRequest.getEmail();
        this.role = RoleEnum.valueOf(userRequest.getRole());
    }

    // 회원 탈퇴
    public void sucession(){
        this.delYn = "Y";
    }
}
