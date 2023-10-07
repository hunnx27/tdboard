package com.twodollar.tdboard.modules.auth.controller;

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
public class AuthUserWebController {

    private final AuthService authService;

    /**
     * 로그인 폼 페이지
     *
     * @return
     */
    @GetMapping("user/login")
    public String loginForm(
            @RequestParam(value="error", required=false)String error,
            @RequestParam(value="exception", required=false)String exception,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "pages/user/login";
    }

    /**
     * 회원 가입 페이지(일반회원)
     *
     * @return
     */
    @GetMapping("user/join")
    public String joinForm() {
        return "pages/user/join";
    }
    /**
     * 회원 가입 페이지(기관회원)
     *
     * @return
     */
    @GetMapping("user/join2")
    public String joinForm2() {
        return "pages/user/join2";
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
     * @param user
     * @return
     */
    @PostMapping("user/join_proc")
    public String join(User user) {
        authService.join(user);
        return "redirect:/user/login";
    }
    @PostMapping("user/join2_proc")
    public String join2(User user) {
        authService.join2(user);
        return "redirect:/user/login";
    }

}
