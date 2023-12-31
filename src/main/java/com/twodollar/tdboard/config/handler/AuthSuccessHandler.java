package com.twodollar.tdboard.config.handler;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private final AuthJwtTokenProvider userJwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        HttpSession session = request.getSession(true);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        String accessToken = userJwtTokenProvider.createToken(userDetails.getUsername(), Collections.singletonList("USER"));
        String refreshToken = userJwtTokenProvider.createRefreshToken(userDetails.getUsername());

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);
        // Refresh Token DB에 저장
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));
        user.setRefreshToken(refreshToken);
    }

}
