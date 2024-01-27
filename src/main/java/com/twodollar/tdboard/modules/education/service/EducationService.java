package com.twodollar.tdboard.modules.education.service;
import com.twodollar.tdboard.modules.education.controller.request.EducationRequest;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.repository.EducationRepository;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {
    private final EducationRepository educationRepository;
    private final FacilityService facilityService;

    public long getTotalEducationSize(){
        return educationRepository.countEducationByDelYn("N");
    }
    public List<Education> getEducations(Pageable pageable) {
        List<Education> list = educationRepository.getEducationsByDelYnOrderByCreatedAtDesc("N", pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 교육이 없습니다."));
        return list;
    }

    public List<Education> getEducationsYearMonth(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        String yearMonth = localDate.format(DateTimeFormatter.ofPattern("YYYY-MM"));
        List<Education> list = educationRepository.getEducationsByStartDateContainingOrEndDateContainingAndDelYn(yearMonth, yearMonth, "N").orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "교육 일정이 없습니다."));
        return list;
    }

    public Education getEducationById(final Long id) {
        return educationRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Education createEducation(final EducationRequest createEducation) {
        if(createEducation == null) throw new IllegalArgumentException("item cannot be null");
        Education education = createEducation.toEntity();

        Long facilityId = createEducation.getFacilityId();
        if(facilityId != null){
            Facility facility =facilityService.getFacilityById(facilityId);
            education.setFacility(facility);
        }

        return educationRepository.save(education);
    }

    public Education updateEducation(final long id, final EducationRequest updateEducation) {
        Education education = getEducationById(id);
        education.setName(updateEducation.getName());
        education.setDescription(updateEducation.getDescription());
        education.setImageUrl(updateEducation.getImageUrl());
        education.setUseYn(updateEducation.getUseYn());
        education.setLocation(updateEducation.getLocation());
        education.setStartDate(updateEducation.getStartDate());
        education.setEndDate(updateEducation.getEndDate());
        education.setApplicationStartDate(updateEducation.getApplicationStartDate());
        education.setApplicationEndDate(updateEducation.getApplicationEndDate());
        education.setManager(updateEducation.getManager());
        education.setCapacity(updateEducation.getCapacity());
        education.setDelYn(updateEducation.getDelYn());
        education.setUpdatedAt(null);

        Long facilityId = updateEducation.getFacilityId();
        if(facilityId != null){
            Facility facility =facilityService.getFacilityById(facilityId);
            education.setFacility(facility);
        }
        return educationRepository.save(education);
    }

    public void deleteEducationById(final Long id) {
        Education education = getEducationById(id);
        education.setDelYn("Y");
        educationRepository.save(education);
    }

}
