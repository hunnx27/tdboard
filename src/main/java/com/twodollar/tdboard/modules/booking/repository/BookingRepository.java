package com.twodollar.tdboard.modules.booking.repository;

import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<List<Booking>> getBookingsByBookingType(BookingType bookingType, Pageable pageable);
    Optional<List<Booking>> getBookingsByUserAndBookingType(User user, BookingType bookingType, Pageable pageable);

}
