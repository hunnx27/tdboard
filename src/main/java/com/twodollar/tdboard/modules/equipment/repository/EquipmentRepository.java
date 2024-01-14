package com.twodollar.tdboard.modules.equipment.repository;

import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // username을 가지고 User 정보를 가져올 수 있게 메소드 생성
    int countAll();
    Optional<List<Equipment>> findAll(String title, Pageable pageable);
    Optional<Equipment> findByById(Long id);
}
