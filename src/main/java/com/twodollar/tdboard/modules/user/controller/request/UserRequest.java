package com.twodollar.tdboard.modules.user.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class UserRequest {
    private String username;
    private String sex;
    private String email;
    private String phone;
    private String birthday;
    private String channel; //유입채널
    private String role;// 권한

    @Builder
    public UserRequest(String username, String sex, String email, String phone, String birthday, String channel, String role) {
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.channel = channel;
        this.role = role;
    }
}
