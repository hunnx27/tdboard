package com.twodollar.tdboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class OrgController {
    @GetMapping("org/sample")
    public String mypage() {
        return "pages/org/sample";
    }
}
