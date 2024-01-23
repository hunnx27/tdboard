package com.twodollar.tdboard.modules.application.service;
import com.twodollar.tdboard.modules.application.controller.request.ApplicationRequest;
import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.application.repository.ApplicationRepository;
import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.service.EducationService;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final EducationService educationService;


    public long getTotalApplicationSize(){
        return applicationRepository.count();
    }
    public List<Application> getApplications(Pageable pageable) {
        List<Application> list = applicationRepository.getApplicationsBy(pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 신청이 없습니다."));
        return list;
    }

    public List<Application> getApplications(String userId, Pageable pageable){
        User user = userService.getUserByUserId(userId);
        List<Application> list = applicationRepository.getApplicationsByUser(user, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 신청이 없습니다."));
        return list;
    }


    public Application getApplicationById(final Long id) {
        return applicationRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Application createApplication(String userId, final ApplicationRequest createApplication) {
        if(createApplication == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        Application application = createApplication.toEntity();
        if(userId!=null){
            User user = userService.getUserByUserId(userId);
            application.setUser(user);
            application.setReqPhone(user.getPhone());
        }
        Long educationId = createApplication.getEducationId();
        if(educationId!=null){
            Education education = educationService.getEducationById(educationId);
            application.setEducation(education);
        }
        return applicationRepository.save(application);
    }

    public Application approvalApplication(final long id, String approvalUserId) {
        User approvalUser = userService.getUserByUserId(approvalUserId);
        Application application = getApplicationById(id);
        application.approval(approvalUser);
        return applicationRepository.save(application);
    }

    public Application cancelApplication(final long id) {
        Application application = getApplicationById(id);
        application.cancel();
        return applicationRepository.save(application);
    }

    public void deleteApplicationByIdAndUserId(final Long id, String userId) {
        User user = userService.getUserByUserId(userId);
        int cnt = applicationRepository.countApplicationByIdAndUser(id, user);
        if(cnt==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다.");
        }
        applicationRepository.deleteById(id);
    }

}
