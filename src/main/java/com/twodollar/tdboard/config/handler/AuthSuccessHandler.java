package com.twodollar.tdboard.config.handler;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private final AuthJwtTokenProvider authJwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(true);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserId(userDetails.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

            if ("Y".equals(user.getDelYn())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다. 관리자에게 문의하세요.");
            }

            String role = "USER";
            if (!userDetails.getAuthorities().isEmpty()) {
                role = String.valueOf(userDetails.getAuthorities().stream().findFirst().get()).replace("ROLE_", "");
            }
            String accessToken =
                    authJwtTokenProvider.createToken(userDetails.getUsername()
                            , Collections.singletonList(role));
            String refreshToken = authJwtTokenProvider.createRefreshToken(userDetails.getUsername());
            log.info("accessToken : {}", accessToken);
            log.info("refreshToken : {}", refreshToken);
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("refreshToken", refreshToken);
            session.setAttribute(role, true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getUsername());


            // JWT 쿠키 저장(쿠키 명 : token)
            Cookie cookie1 = new Cookie("accessToken", accessToken);
            cookie1.setPath("/");
            cookie1.setMaxAge(60 * 60 * 24 * 1); // 유효기간 1일
            // cookie1.setHttpOnly(false);// httoOnly 옵션을 추가해 서버만 쿠키에 접근할 수 있게 설정

            Cookie cookie2 = new Cookie("refreshToken", refreshToken);
            cookie2.setPath("/");
            cookie2.setMaxAge(60 * 60 * 24 * 1); // 유효기간 1일
            // cookie2.setHttpOnly(false);// httoOnly 옵션을 추가해 서버만 쿠키에 접근할 수 있게 설정

            response.addCookie(cookie1);
            response.addCookie(cookie2);
            super.onAuthenticationSuccess(request, response, authentication);
            // Refresh Token DB에 저장
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }catch(ResponseStatusException e){
            String errorMessage = URLEncoder.encode(e.getReason(), "UTF-8");
            response.sendRedirect("/auth/login?error=true&exception="+errorMessage);
        }
        catch(Exception e){
            String errorMessage = URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect("/auth/login?error=true&exception="+errorMessage);
        }
    }

}
