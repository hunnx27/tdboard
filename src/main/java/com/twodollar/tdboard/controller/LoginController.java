package com.twodollar.tdboard.controller;

import com.twodollar.tdboard.model.User;
import com.twodollar.tdboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository; // 글 아래에서 생성할 예정
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 시큐리티에서 빈(Bean) 생성할 예정

    /**
     * // TODO 삭제
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
    @GetMapping("user/loginForm")
    public String loginForm(
            @RequestParam(value="error", required=false)String error,
            @RequestParam(value="exception", required=false)String exception,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "pages/user/loginForm";
    }

    /**
     * 회원 가입 페이지(일반회원)
     *
     * @return
     */
    @GetMapping("user/joinForm")
    public String joinForm() {
        return "pages/user/joinForm";
    }

    /**
     * 회원 가입이 실행되는 부분
     *
     * @param user
     * @return
     */
    @PostMapping("user/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user/loginForm";
    }
    @PostMapping("user/join2")
    public String join2(User user) {
        user.setRole("ROLE_ORG");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user/loginForm";
    }
    /**
     * 회원 가입 페이지(기관회원)
     *
     * @return
     */
    @GetMapping("user/joinForm2")
    public String joinForm2() {
        return "pages/user/joinForm2";
    }
}
