package com.twodollar.tdboard.modules.admin.controller;

import com.twodollar.tdboard.modules.admin.entity.Admin;
import com.twodollar.tdboard.modules.admin.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
