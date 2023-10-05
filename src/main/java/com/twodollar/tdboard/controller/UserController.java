package com.twodollar.tdboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserController {
    /**
     * 내 페이지
     *
     * @return
     */
    @GetMapping("user/mypage")
    public String mypage() {
        return "pages/user/mypage";
    }
}
