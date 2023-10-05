package com.twodollar.tdboard.repository;

import com.twodollar.tdboard.model.Admin;
import com.twodollar.tdboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // username을 가지고 Admin 정보를 가져올 수 있게 메소드 생성
    Optional<Admin> findByUsername(String username);
}
