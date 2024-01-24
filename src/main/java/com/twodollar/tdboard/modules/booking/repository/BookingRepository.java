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
    Optional<List<Booking>> getBookingsByUser(User user, Pageable pageable);
    Optional<List<Booking>> getBookingsByBookingType(BookingType bookingType, Pageable pageable);

    @Query(value = "select b from Booking b where b.bookingType = :bookingType and b.approvalYn ='Y' and (b.startAt between :starttime and :endtime or b.endAt between :starttime and :endtime)")
    Optional<List<Booking>> getBookingsAvailableNative(@Param(value = "bookingType")BookingType bookingType, @Param(value = "starttime")LocalDateTime starttime, @Param(value = "endtime")LocalDateTime endtime);
//    Optional<List<Booking>> getBookingsByBookingTypeAndStartAtBetweenOrEndAtBetweenAndApprovalYn(BookingType bookingType,
//                                                                                                 String targetDate1, String targetDate2,
//                                                                                                 String targetDate3, String targetDate4,
//                                                                                                 String approvalYn, Pageable pageable);
    Optional<List<Booking>> getBookingsByUserAndBookingType(User user, BookingType bookingType, Pageable pageable);

    int countByBookingType(BookingType bookingType);

    int countByUser(User user);
    int countByUserAndBookingType(User user, BookingType bookingType);
    int countBookingByIdAndUser(Long id, User user);

}
