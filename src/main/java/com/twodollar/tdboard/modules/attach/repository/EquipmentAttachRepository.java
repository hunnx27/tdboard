package com.twodollar.tdboard.modules.attach.repository;

import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.attach.entity.EquipmentAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EquipmentAttachRepository extends JpaRepository<EquipmentAttach, Long> {
    Optional<List<EquipmentAttach>> getEquipmentAttachesByEquipment(Equipment equipment);

    void deleteEquipmentAttachesByEquipment(Equipment equipment);


}
