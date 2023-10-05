package com.twodollar.tdboard.controller;

import com.twodollar.tdboard.model.Admin;
import com.twodollar.tdboard.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository; // 글 아래에서 생성할 예정
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 시큐리티에서 빈(Bean) 생성할 예정

    /**
     * 유저 페이지
     *
     * @return
     */
    @GetMapping("admin")
    public String user() {
        return "pages/admin/index";
    }

    /**
     * 로그인 폼 페이지
     *
     * @return
     */
    @GetMapping("admin/loginForm")
    public String loginForm() {
        return "pages/admin/loginForm";
    }

    /**
     * 회원 가입 페이지
     *
     * @return
     */
    @GetMapping("admin/joinForm")
    public String joinForm() {
        return "pages/admin/joinForm";
    }

    /**
     * 회원 가입이 실행되는 부분
     *
     * @param admin
     * @return
     */
    @PostMapping("admin/join")
    public String join(Admin admin) {
        admin.setRole("ROLE_ADMIN"); // 권한 정보는 임시로 ROLE_ADMIN으로 넣는다.
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return "redirect:/admin/loginForm";
    }
}
