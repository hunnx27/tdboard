package com.twodollar.tdboard.modules.equipment.repository;

import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    int countByFacilityId(Long facilityId);
    Optional<List<Equipment>> getEquipmentsByFacilityId(Long facilityId, Pageable pageable);

}
