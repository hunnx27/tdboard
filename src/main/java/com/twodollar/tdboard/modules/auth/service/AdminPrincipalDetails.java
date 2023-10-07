package com.twodollar.tdboard.modules.auth.service;

import com.twodollar.tdboard.modules.admin.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AdminPrincipalDetails implements UserDetails {

    private Admin admin;

    public AdminPrincipalDetails(Admin admin) {
        this.admin = admin;
    }

    /**
     * 해당 유저의 권한을 가져오는 메소드
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(admin.getRole()));
        return authorities;
    }

    /**
     * 비밀번호를 가지고 오는 메소드
     *
     * @return
     */
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    /**
     * 계정 만료 확인
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 확인
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정 비밀번호 변경 확인
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 확인
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
