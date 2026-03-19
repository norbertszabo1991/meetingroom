package hu.meetingroom.service;

import com.example.meetingroom.model.Booking;
import hu.meetingroom.exception.BookingNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    com.example.meetingroom.model.Booking createBooking(com.example.meetingroom.model.Booking bookingRequest);
    List<Booking> getBookings(Long roomId, LocalDateTime from, LocalDateTime to);

    void deleteBooking(Long bookingId) throws BookingNotFoundException;
    }
