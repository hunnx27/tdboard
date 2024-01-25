package com.twodollar.tdboard.modules.application.repository;

import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<List<Application>> getApplicationsBy(Pageable pageable);
    Optional<List<Application>> getApplicationsByUser(User user, Pageable pageable);

    int countApplicationByIdAndUser(Long id, User user);

}
