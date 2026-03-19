package hu.meetingroom.service.impl;

import hu.meetingroom.domain.entity.Booking;
import hu.meetingroom.exception.BookingNotFoundException;
import hu.meetingroom.mapper.BookingMapper;
import hu.meetingroom.repository.BookingRepository;
import hu.meetingroom.repository.MeetingRoomRepository;
import hu.meetingroom.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final MeetingRoomRepository roomRepository;
    private final BookingMapper mapper;

    public BookingServiceImpl(BookingRepository bookingRepository, MeetingRoomRepository roomRepository, BookingMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    public com.example.meetingroom.model.Booking createBooking(com.example.meetingroom.model.Booking bookingRequest) {
        // 1. Tárgyaló létezik-e?
        roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Tárgyaló nem található: " + bookingRequest.getRoomId()));

        // 2. Érvényes időintervallum?
        if (bookingRequest.getFrom().isAfter(bookingRequest.getTo())) {
            throw new IllegalArgumentException("Kezdés nem lehet a befejezés után");
        }

        // 3. ÜTKÖZÉS ELLENŐRZÉS
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                bookingRequest.getRoomId(),
                bookingRequest.getFrom().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(),
                bookingRequest.getTo().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
        );

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Ütközés meglévő foglalással: " + overlapping.size() + " db");
        }

        // 4. Új foglalás mentés
        Booking entity = mapper.toEntity(bookingRequest);
        entity.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Booking saved = bookingRepository.save(entity);

        return mapper.toDto(saved);
    }

    public List<com.example.meetingroom.model.Booking> getBookings(Long roomId, LocalDateTime from, LocalDateTime to) {
        List<Booking> bookings = bookingRepository.findBookings(roomId, from, to);

        // Rendezés kezdés szerint
        bookings.sort(Comparator.comparing(Booking::getFrom));

        return mapper.toDtoList(bookings);
    }

    public void deleteBooking(Long bookingId) throws BookingNotFoundException {
        // Meglévő-e?
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNotFoundException("Foglalás nem található: " + bookingId);
        }

        // Törlés
        bookingRepository.deleteById(bookingId);
    }
}
