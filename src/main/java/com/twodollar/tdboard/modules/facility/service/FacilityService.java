package com.twodollar.tdboard.modules.facility.service;
import com.twodollar.tdboard.modules.facility.controller.request.FacilityRequest;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.twodollar.tdboard.modules.facility.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public long getTotalFacilitySize(){
        return facilityRepository.countFacilityByDelYn("N");
    }
    public List<Facility> getFacilitys(Pageable pageable) {
        List<Facility> list = facilityRepository.getFacilitiesByDelYnOrderByCreatedAtDesc("N", pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 시설이 없습니다."));
        if(list.size() == 0){
            new IllegalArgumentException("no such data");
        }
        return list;
    }

    public Facility getFacilityById(final Long id) {
        return facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Facility createFacility(final FacilityRequest createFacility) {
        if(createFacility == null) throw new IllegalArgumentException("item cannot be null");
        return facilityRepository.save(createFacility.toEntity());
    }

    public Facility updateFacility(final long id, final FacilityRequest updateFacility) {
        Facility facility = getFacilityById(id);
        facility.setName(updateFacility.getName());
        facility.setDescription(updateFacility.getDescription());
        facility.setImageUrl(updateFacility.getImageUrl());
        facility.setUseYn(updateFacility.getUseYn());
        facility.setDelYn(updateFacility.getDelYn());
        facility.setUpdatedAt(null);
        return facilityRepository.save(facility);
    }

    public void deleteFacilityById(final Long id) {
        Facility facility = getFacilityById(id);
        facility.setDelYn("Y");
        facilityRepository.save(facility);
    }

}
