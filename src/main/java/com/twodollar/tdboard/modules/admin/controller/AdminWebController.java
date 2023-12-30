package com.twodollar.tdboard.modules.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AdminWebController {

    /**
     * 유저 페이지
     *
     * @return
     */
    @GetMapping("admin")
    public String user() {
        return "pages/admin/index";
    }


}
