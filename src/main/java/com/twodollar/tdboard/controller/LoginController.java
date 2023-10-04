package com.twodollar.tdboard.controller;

import com.twodollar.tdboard.model.User;
import com.twodollar.tdboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository; // 글 아래에서 생성할 예정
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 시큐리티에서 빈(Bean) 생성할 예정

    /**
     * 유저 페이지
     *
     * @return
     */
    @GetMapping("user")
    public String user() {
        return "pages/user";
    }

    /**
     * 로그인 폼 페이지
     *
     * @return
     */
    @GetMapping("loginForm")
    public String loginForm() {
        return "pages/loginForm";
    }

    /**
     * 회원 가입 페이지
     *
     * @return
     */
    @GetMapping("joinForm")
    public String joinForm() {
        return "pages/joinForm";
    }

    /**
     * 회원 가입이 실행되는 부분
     *
     * @param user
     * @return
     */
    @PostMapping("join")
    public String join(User user) {
        user.setRole("ROLE_ADMIN"); // 권한 정보는 임시로 ROLE_ADMIN으로 넣는다.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/loginForm";
    }
}
