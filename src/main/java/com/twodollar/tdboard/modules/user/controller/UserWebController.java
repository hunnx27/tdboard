package com.twodollar.tdboard.modules.user.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserWebController {
    /**
     * 내 페이지
     *
     * @return
     */
    @GetMapping("user/mypage")
    public String mypage(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 정보를 세션에 저장
        // session.setAttribute("username", authentication.getName());
        model.addAttribute("username", authentication.getName());
        return "pages/user/mypage";
    }
    /**
     * 회원 탈퇴
     * @return
     */
    @GetMapping("user/secession")
    public String secession() {
        return "pages/user/secession";
    }
    /**
     * 공유이력(시설/장비)
     * @return
     */
    @GetMapping("user/share-list")
    public String shareList() {
        return "pages/user/share-list";
    }
    /**
     * 교육이력
     * @return
     */
    @GetMapping("user/edu-list")
    public String eduList() {
        return "pages/user/edu-list";
    }
}
