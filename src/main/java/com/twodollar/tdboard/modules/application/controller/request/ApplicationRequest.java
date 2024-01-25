package com.twodollar.tdboard.modules.application.controller.request;

import com.twodollar.tdboard.modules.application.entity.Application;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class ApplicationRequest {
    private Long id;
    private String reqPhone;
    private Long educationId;

    public Application toEntity(){
        return Application.builder().
                id(this.id).
                reqPhone(this.reqPhone).
                build();
    }
}
