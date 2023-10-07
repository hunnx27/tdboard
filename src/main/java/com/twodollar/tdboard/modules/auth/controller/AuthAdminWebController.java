package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.admin.entity.Admin;
import com.twodollar.tdboard.modules.auth.service.AuthService;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthAdminWebController {

    private final AuthService authService;

    /**
     * 로그인 폼 페이지
     *
     * @return
     */
    @GetMapping("admin/login")
    public String loginForm() {
        return "pages/admin/login";
    }

    /**
     * 회원 가입 페이지
     *
     * @return
     */
    @GetMapping("admin/join")
    public String joinForm() {
        return "pages/admin/join";
    }

    /**
     *
     *
     * ## 서비스 기능  ##
     *
     *
     */

    /**
     * 회원 가입이 실행되는 부분
     *
     * @param admin
     * @return
     */
    @PostMapping("admin/join_proc")
    public String join(Admin admin) {
        authService.adminJoin(admin);
        return "redirect:/admin/login";
    }

}
