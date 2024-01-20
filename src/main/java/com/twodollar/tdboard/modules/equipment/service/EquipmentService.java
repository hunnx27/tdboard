package com.twodollar.tdboard.modules.equipment.service;
import com.twodollar.tdboard.modules.equipment.controller.request.EquipmentRequest;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.repository.EquipmentRepository;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.repository.FacilityRepository;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final FacilityService facilityService;

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

    public Equipment createEquipment(final EquipmentRequest createEquipment) {
        if(createEquipment == null) throw new IllegalArgumentException("item cannot be null");

        Equipment equipment = createEquipment.toEntity();
        Long facilityId = createEquipment.getFacilityId();
        if(facilityId!=null){
            Facility facility = facilityService.getFacilityById(facilityId);
            equipment.setFacility(facility);
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(final long id, final EquipmentRequest updateEquipment) {
        Equipment equipment = getEquipmentById(id);
        equipment.setLocation(updateEquipment.getLocation());
        equipment.setName(updateEquipment.getName());
        equipment.setDescription(updateEquipment.getDescription());
        equipment.setImageUrl(updateEquipment.getImageUrl());
        equipment.setUseYn(updateEquipment.getUseYn());
        equipment.setDelYn(updateEquipment.getDelYn());
        equipment.setUpdatedAt(null);

        Long facilityId = updateEquipment.getFacilityId();
        if(facilityId != null){
            Facility facility =facilityService.getFacilityById(facilityId);
            equipment.setFacility(facility);
        }
        return equipmentRepository.save(equipment);
    }

    public void deleteEquipmentById(final Long id) {
        Equipment equipment = getEquipmentById(id);
        equipment.setDelYn("Y");
        equipmentRepository.save(equipment);
    }

}