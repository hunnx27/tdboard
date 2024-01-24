package com.twodollar.tdboard.modules.booking.service;
import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.booking.controller.request.BookingRequest;
import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.booking.repository.BookingRepository;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.service.EducationService;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.service.EquipmentService;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final EducationService educationService;
    private final FacilityService facilityService;
    private final EquipmentService equipmentService;

    public long getTotalBookingSize(){
        return bookingRepository.count();
    }
    public long getTotalBookingSize(BookingType bookingType){
        return bookingRepository.countByBookingType(bookingType);
    }

    public long getTotalBookingSize(User user){
        return bookingRepository.countByUser(user);
    }
    public long getTotalBookingSize(User user, BookingType bookingType){
        return bookingRepository.countByUserAndBookingType(user, bookingType);
    }

    public List<Booking> getBookings(User user, BookingType bookingType, Pageable pageable) {
        List<Booking> list = bookingRepository.getBookingsByUserAndBookingType(user, bookingType, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        return list;
    }
    public List<Booking> getBookings(User user, Pageable pageable) {
        List<Booking> list = bookingRepository.getBookingsByUser(user, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        return list;
    }
    public List<Booking> getBookings(BookingType bookingType, Pageable pageable) {
        List<Booking> list = bookingRepository.getBookingsByBookingType(bookingType, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        return list;
    }

    public List<Integer> getAvailableBookingTimes(BookingType bookingType, String targetDate, Pageable pageable) {
        LocalDateTime starttime = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
        LocalDateTime endtime = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);
        List<Booking> bookings = bookingRepository.getBookingsAvailableNative(bookingType, starttime, endtime).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        return extractTimeByBookings(bookings, targetDate);
    }

    private List<Integer> extractTimeByBookings(List<Booking> bookings, String targetDate){
        List<String> list = new ArrayList<>();
        List<Integer> extractedtimes = bookings.stream()
                .map((booking)->{
                    List<LocalDateTime> times = this.extractTimeRange(booking.getStartAt(), booking.getEndAt());
                    return times;
                })
                .flatMap(localDateTimes -> localDateTimes.stream())//여러 리스트 합치기
                .sorted()
                .filter(localDateTime -> targetDate.equals(localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))  //타겟 일자만 추출
                .distinct()
                .peek(localDateTime -> log.info("{}:{}", localDateTime.getHour(), localDateTime.getMinute()))
                .map(localDateTime -> localDateTime.getHour())
                .collect(Collectors.toList());
        return extractedtimes;
    }

    // 사이의 시간 목록 추출
    private List<LocalDateTime> extractTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<LocalDateTime> timeRangeList = new ArrayList<>();

        LocalDateTime currentDateTime = startDateTime;

        while (!currentDateTime.isAfter(endDateTime)) {
            timeRangeList.add(currentDateTime);
            currentDateTime = currentDateTime.plusHours(1); // 시간 간격을 조절할 수 있습니다. 여기서는 1시간 간격으로 설정했습니다.
        }

        return timeRangeList;
    }

    public long getTotalBookingSize(String userId, BookingType bookingType, Pageable pageable){
        return bookingRepository.count();
    }

    public List<Booking> getBookings(String userId, BookingType bookingType, Pageable pageable) {
        User user = userService.getUserByUserId(userId);
        List<Booking> list = bookingRepository.getBookingsByUserAndBookingType(user, bookingType, pageable).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        return list;
    }

    public Booking getBookingById(final Long id) {
        return bookingRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
    }

    public Booking createBooking(String userId, final BookingRequest createBooking) {
        if(createBooking == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

        Booking booking = createBooking.toEntity();

        if(userId!=null){
            User user = userService.getUserByUserId(userId);
            booking.setUser(user);
        }
        Long educationId = createBooking.getEducationId();
        if(educationId!=null){
            Education education = educationService.getEducationById(educationId);
            booking.setEducation(education);
        }
        Long facilityId = createBooking.getFacilityId();
        if(facilityId!=null){
            Facility facility = facilityService.getFacilityById(facilityId);
            booking.setFacility(facility);
        }
        Long equipmentId = createBooking.getEquipmentId();
        if(equipmentId!=null){
            Equipment equipment = equipmentService.getEquipmentById(equipmentId);
            booking.setEquipment(equipment);
        }

        // 연결된 예약 생성
        this.relatedBookingCreated(booking);

        return bookingRepository.save(booking);
    }

    private void relatedBookingCreated(Booking booking){
        // TODO 관련 예약 생성..
        log.error("[TODO]관련 예약 생성 미구현");
    }
    private void relatedBookingDelete(Booking booking){
        // TODO 관련 예약 삭제..
        log.error("[TODO]관련 예약 삭제 미구현");
    }


    public Booking approvalBooking(final long id, String approvalUserId) {
        User approvalUser = userService.getUserByUserId(approvalUserId);
        Booking booking = getBookingById(id);
        booking.approval(approvalUser);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(final long id) {
        Booking booking = getBookingById(id);
        booking.cancel();
        return bookingRepository.save(booking);
    }

    public void deleteBookingById(final Long id, String userId) {
        User user = userService.getUserByUserId(userId);
        int cnt = bookingRepository.countBookingByIdAndUser(id, user);
        if(cnt==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다.");
        }
        Booking booking = bookingRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."));
        bookingRepository.deleteById(id);
        this.relatedBookingDelete(booking);
    }

}
