package hu.meetingroom.repository;

import hu.meetingroom.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository  extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId " +
            "AND ((b.from < :to AND b.to > :from))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:roomId IS NULL OR b.roomId = :roomId) AND " +
            "(:from IS NULL OR b.from >= :from) AND " +
            "(:to IS NULL OR b.to <= :to)")
    List<Booking> findBookings(@Param("roomId") Long roomId,
                                     @Param("from") LocalDateTime from,
                                     @Param("to") LocalDateTime to);
}

