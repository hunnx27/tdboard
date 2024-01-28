package com.twodollar.tdboard.modules.attach.repository;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.attach.entity.FacilityAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityAttachRepository extends JpaRepository<FacilityAttach, Long> {
    Optional<List<FacilityAttach>> getFacilityAttachesByFacility(Facility facility);

    void deleteFacilityAttachesByFacility(Facility facility);


}
