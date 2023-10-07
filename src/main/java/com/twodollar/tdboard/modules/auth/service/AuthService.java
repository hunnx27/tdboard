package com.twodollar.tdboard.modules.auth.service;

import com.twodollar.tdboard.modules.admin.entity.Admin;
import com.twodollar.tdboard.modules.admin.repository.AdminRepository;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository; // 글 아래에서 생성할 예정
    private final AdminRepository adminRepository; // 글 아래에서 생성할 예정
    private final BCryptPasswordEncoder passwordEncoder; // 시큐리티에서 빈(Bean) 생성할 예정

    public void join(User user){
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void join2(User user){
        user.setRole("ROLE_ORG");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void adminJoin(Admin admin){
        admin.setRole("ROLE_ADMIN"); // 권한 정보는 임시로 ROLE_ADMIN으로 넣는다.
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }


}
