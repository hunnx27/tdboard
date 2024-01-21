package com.twodollar.tdboard.modules.auth.controller.request;

import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.entity.enums.ChannelEnum;
import com.twodollar.tdboard.modules.user.entity.enums.SexEnum;
import lombok.Getter;

@Getter
public class UserFindRequest {
    private String userId;
    private String username;
    private String email;
}
