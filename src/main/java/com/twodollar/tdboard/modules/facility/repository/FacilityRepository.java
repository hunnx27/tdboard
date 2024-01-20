package com.twodollar.tdboard.modules.facility.repository;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

}
