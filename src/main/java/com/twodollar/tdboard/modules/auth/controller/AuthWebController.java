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
public class AuthWebController {

    private final AuthService authService;

    /**
     * 로그인 폼 페이지
     *
     * @return
     */
    @GetMapping("auth/login")
    public String loginForm(
            @RequestParam(value="error", required=false)String error,
            @RequestParam(value="exception", required=false)String exception,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "pages/auth/login";
    }

    /**
     * 회원 가입 페이지(일반회원)
     *
     * @return
     */
    @GetMapping("auth/join")
    public String joinForm() {
        return "pages/auth/join";
    }
    /**
     * 회원 가입 이용약관 페이지(일반회원)
     *
     * @return
     */
    @GetMapping("auth/join/terms")
    public String joinTerms() {
        return "pages/auth/join-terms";
    }
    /**
     * 회원 가입 페이지(기관회원)
     *
     * @return
     */
    @GetMapping("auth/join2")
    public String joinForm2() {
        return "pages/auth/join2";
    }

    /**
     * 회원 가입 페이지(관리자)
     *
     * @return
     */
    @GetMapping("auth/join3")
    public String joinForm3() {
        return "pages/auth/join3";
    }

    /**
     * 아이디 찾기
     * @return
     */
    @GetMapping("auth/find/id")
    public String findId() {
        return "pages/auth/id-find";
    }
    /**
     * 비밀번호 찾기
     * @return
     */
    @GetMapping("auth/find/password")
    public String findPassword() {
        return "pages/auth/password-find";
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
    @PostMapping("auth/join_proc")
    public String join(User user) {
        authService.join(user);
        return "redirect:/auth/login";
    }
    @PostMapping("auth/join2_proc")
    public String join2(User user) {
        authService.join_orgtest(user);
        return "redirect:/auth/login";
    }

    @PostMapping("auth/join3_proc")
    public String join3(User user) {
        authService.join_admintest(user);
        return "redirect:/auth/login";
    }

}
