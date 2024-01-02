package com.twodollar.tdboard.config.handler;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        super.onAuthenticationSuccess(request, response, authentication);
        HttpSession session = request.getSession(true);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String role = "USER";
        if(!userDetails.getAuthorities().isEmpty()){
            role = String.valueOf(userDetails.getAuthorities().stream().findFirst().get()).replace("ROLE_","");
        }
        String accessToken =
                authJwtTokenProvider.createToken(userDetails.getUsername()
                                        , Collections.singletonList(role));
        String refreshToken = authJwtTokenProvider.createRefreshToken(userDetails.getUsername());
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);
        // Refresh Token DB에 저장
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

}
