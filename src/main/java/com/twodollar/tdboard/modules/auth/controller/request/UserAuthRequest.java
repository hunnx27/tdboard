package com.twodollar.tdboard.modules.auth.controller.request;

import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.entity.enums.ChannelEnum;
import com.twodollar.tdboard.modules.user.entity.enums.RoleEnum;
import com.twodollar.tdboard.modules.user.entity.enums.SexEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAuthRequest {
    private String userId;
    private String username;
    private String password;
    private String sex;
    private String email;
    private String phone;
    private String birthday;
    private String channel; //유입채널

    public User toEntity(){
        return User.builder()
            .userId(this.userId)
            .username(this.username)
            .sex(SexEnum.valueOf(this.sex))
            .email(this.email)
            .phone(this.phone)
            .birthday(this.birthday)
            .channel(ChannelEnum.valueOf(this.channel))
            // .password(this.password) // 플레인이라 회원가입로직시 암호화 후 set함
            // .role(this.role) // 롤도 로직에 따라 set함
            .build();
    }
}
