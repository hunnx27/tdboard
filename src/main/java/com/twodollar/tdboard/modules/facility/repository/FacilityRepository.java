package com.twodollar.tdboard.modules.facility.repository;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    int countFacilityByDelYn(String delYn);
    Optional<List<Facility>> getFacilitiesByDelYnOrderByCreatedAtDesc(String DelYn, Pageable pageable);

}
