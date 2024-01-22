package com.twodollar.tdboard.modules.user.repository;

import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // username을 가지고 User 정보를 가져올 수 있게 메소드 생성
    Optional<User> findByUserId(String userId);
    int countByUserId(String userId);
    int countByEmail(String email);
    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User> findByUsernameAndUserIdAndEmail(String username, String userId, String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByRefreshToken(String refreshToken);

    int countUserByUserId(String userId);
    int countUserByEmail(String email);
}
