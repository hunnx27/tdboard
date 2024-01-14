package com.twodollar.tdboard.modules.equipment.service;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public long getTotalEquipmentSize(){
        return equipmentRepository.count();
    }
    public List<Equipment> getEquipments(Pageable pageable) {
        List<Equipment> list = equipmentRepository.findAll(pageable).getContent();
        if(list.size() == 0){
            new IllegalArgumentException("no such data");
        }
        return list;
    }

    public Equipment getEquipmentById(final Long id) {
        return equipmentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Equipment createEquipment(final Equipment createEquipment) {
        if(createEquipment == null) throw new IllegalArgumentException("item cannot be null");
        return equipmentRepository.save(createEquipment);
    }

    public Equipment updateEquipment(final long id, final Equipment updateEquipment) {
        Equipment Equipment = getEquipmentById(id);
        Equipment.setTitle(updateEquipment.getTitle());
        Equipment.setContext(updateEquipment.getContext());
        return equipmentRepository.save(Equipment);
    }

    public void deleteEquipmentById(final Long id) {
        equipmentRepository.deleteById(id);
    }

}
