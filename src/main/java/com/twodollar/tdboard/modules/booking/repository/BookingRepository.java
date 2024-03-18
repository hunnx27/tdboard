package com.twodollar.tdboard.modules.booking.repository;

import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<List<Booking>> getBookingsByUserAndBookingTypeNot(User user, BookingType bookingType, Pageable pageable);

    @Query(value = "select b from Booking b where b.bookingType = :bookingType and not (b.approvalYn = 'N' and b.approvalAt IS NOT NULL)")
    Optional<List<Booking>> getBookingsByBookingTypeJPQL(BookingType bookingType, Pageable pageable);

    @Query(value = "select b from Booking b where b.bookingType = 'FACILITY' and b.facility.id = :targetId and (b.startAt between :starttime and :endtime or b.endAt between :starttime and :endtime) and not (b.approvalYn = 'N' and b.approvalAt IS NOT NULL)")
    Optional<List<Booking>> getBookingsAvailableFacilityJPQL(@Param(value = "targetId")Long targetId,
                                                       @Param(value = "starttime")LocalDateTime starttime,
                                                       @Param(value = "endtime")LocalDateTime endtime);
    @Query(value = "select b from Booking b where b.bookingType = 'EQUIPMENT' and b.equipment.id = :targetId and (b.startAt between :starttime and :endtime or b.endAt between :starttime and :endtime) and not (b.approvalYn = 'N' and b.approvalAt IS NOT NULL)")
    Optional<List<Booking>> getBookingsAvailableEquipmentJPQL(@Param(value = "targetId")Long targetId,
                                                       @Param(value = "starttime")LocalDateTime starttime,
                                                       @Param(value = "endtime")LocalDateTime endtime);

    Optional<List<Booking>> getBookingsByUserAndBookingType(User user, BookingType bookingType, Pageable pageable);

    @Query(value = "select COUNT(b) from Booking b where b.bookingType = :bookingType and not (b.approvalYn = 'N' and b.approvalAt IS NOT NULL)")
    int countByBookingTypeJPQL(BookingType bookingType);

    int countByUserAndBookingTypeNot(User user, BookingType bookingType);
    int countByUserAndBookingType(User user, BookingType bookingType);
    int countBookingByIdAndUser(Long id, User user);

}
