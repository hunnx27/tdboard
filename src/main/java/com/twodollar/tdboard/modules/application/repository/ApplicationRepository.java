package com.twodollar.tdboard.modules.application.repository;

import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query(value = "select a from Application a where not (a.approvalYn = 'N' and a.approvalAt IS NOT NULL)")
    Optional<List<Application>> getApplicationsByJPQL(Pageable pageable);
    Optional<List<Application>> getApplicationsByUser(User user, Pageable pageable);
    int countApplicationByUser(User user);
    int countApplicationByIdAndUser(Long id, User user);

    @Query(value = "select COUNT(a) from Application a where not (a.approvalYn = 'N' and a.approvalAt IS NOT NULL)")
    int countApplicationsByJPQL();

}
