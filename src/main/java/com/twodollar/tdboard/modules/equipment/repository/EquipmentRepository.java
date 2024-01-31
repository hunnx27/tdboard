package com.twodollar.tdboard.modules.equipment.repository;

import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    int countEquipmentByDelYn(String delYn);
    Optional<List<Equipment>> getEquipmentsByDelYnOrderByCreatedAtDesc(String DelYn, Pageable pageable);

    int countByFacilityIdAndDelYn(Long facilityId, String DelYn);
    Optional<List<Equipment>> getEquipmentsByFacilityIdAndDelYn(Long facilityId, String DelYn, Pageable pageable);

}
