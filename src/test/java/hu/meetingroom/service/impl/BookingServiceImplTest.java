package hu.meetingroom.service.impl;

import com.example.meetingroom.model.Booking;
import hu.meetingroom.domain.entity.MeetingRoom;
import hu.meetingroom.exception.BookingNotFoundException;
import hu.meetingroom.mapper.BookingMapper;
import hu.meetingroom.repository.BookingRepository;
import hu.meetingroom.repository.MeetingRoomRepository;
import hu.meetingroom.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MeetingRoomRepository roomRepository;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingServiceImpl service;

    private Booking validRequest;
    private MeetingRoom validRoom;

    @BeforeEach
    void setUp() {
        OffsetDateTime from = OffsetDateTime.parse("2026-03-06T12:00:00Z");
        OffsetDateTime to = OffsetDateTime.parse("2026-03-06T13:00:00Z");

        validRequest = new Booking(1L, from, to);
        validRequest.setTitle("Valid meeting");
        validRoom = new MeetingRoom(1L, "Nagy tárgyaló", "2. emelet", 10);
    }

    @Test
    void createBookingSuccessTest() {
        // Given
        when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any())).thenReturn(List.of());
        Booking savedBooking = new Booking(123L, validRequest.getFrom(), validRequest.getTo());
        savedBooking.setId(1L);
        savedBooking.setTitle("Valid meeting");
        savedBooking.setCreatedAt(OffsetDateTime.now());
        when(mapper.toEntity(validRequest)).thenReturn(new hu.meetingroom.domain.entity.Booking());
        when(mapper.toDto(any())).thenReturn(savedBooking);

        // When
        Booking result = service.createBooking(validRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRoomId()).isEqualTo(123L);
        verify(roomRepository).findById(1L);
        verify(bookingRepository).findOverlappingBookings(eq(1L), eq(validRequest.getFrom().toLocalDateTime()), eq(validRequest.getTo().toLocalDateTime()));
        verify(bookingRepository).save(any());
    }

    @Test
    void createBookingThrowRoomNotFoundTest() {
        // Given
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        Booking booking = new Booking(999L, OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
        booking.setTitle("test");
        // When/Then
        assertThatThrownBy(() -> service.createBooking(booking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tárgyaló nem található");
    }

    @Test
    void createBookingInnvalidTimeRangeThrowsExceptionTest() {
        // Given
        OffsetDateTime futureTo = OffsetDateTime.now().plusHours(1);
        OffsetDateTime pastFrom = OffsetDateTime.now(); // from > to
        Booking invalidTime = new Booking(1L, futureTo, pastFrom);
        invalidTime.setTitle("Invalid");
        when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));

        // When/Then
        assertThatThrownBy(() -> service.createBooking(invalidTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kezdés nem lehet a befejezés után");
    }

    @Test
    void createBookingOverlapConflictThrowsExceptionTest() {
        // Given
        when(roomRepository.findById(1L)).thenReturn(Optional.of(validRoom));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any()))
                .thenReturn(List.of(mock(hu.meetingroom.domain.entity.Booking.class)));

        // When/Then
        assertThatThrownBy(() -> service.createBooking(validRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ütközés meglévő foglalással");
    }

    @Test
    void getBookingsReturnsFilteredBookingsTest() {
        // Given
        List<hu.meetingroom.domain.entity.Booking> mockBookings = List.of();
        when(bookingRepository.findBookings(1L, null, null)).thenReturn(mockBookings);
        List<Booking> expected = List.of();
        when(mapper.toDtoList(mockBookings)).thenReturn(expected);

        // When
        List<Booking> result = service.getBookings(1L, null, null);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(bookingRepository).findBookings(eq(1L), isNull(), isNull());
    }

    @Test
    void deleteBookingTest() {
        // Given
        when(bookingRepository.existsById(1L)).thenReturn(true);

        // When
        service.deleteBooking(1L);

        // Then
        verify(bookingRepository).existsById(1L);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void deleteBookingNotFoundTest() {
        // Given
        when(bookingRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> service.deleteBooking(999L))
                .isInstanceOf(BookingNotFoundException.class);
    }
}