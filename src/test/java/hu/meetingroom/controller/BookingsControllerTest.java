package hu.meetingroom.controller;

import com.example.meetingroom.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.meetingroom.controller.exception.GlobalExceptionHandler;
import hu.meetingroom.exception.BookingNotFoundException;
import hu.meetingroom.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {BookingsController.class, GlobalExceptionHandler.class})
class BookingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService service;

    @Autowired
    private ObjectMapper objectMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Test
    void createBookingTest() throws Exception {
        // Given - OffsetDateTime API formátum
        OffsetDateTime from = OffsetDateTime.parse("2026-03-06T12:00:00Z");
        OffsetDateTime to = OffsetDateTime.parse("2026-03-06T13:00:00Z");

        Booking request = new Booking(1L, from, to);
        request.setTitle("Ebéd utáni meeting");

        OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        Booking response = new Booking(1L, from, to);
        response.setId(123L);
        response.setTitle("Ebéd utáni meeting");
        response.setCreatedAt(createdAt);
        when(service.createBooking(any(Booking.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.from").value("2026-03-06T12:00:00Z"))
                .andExpect(jsonPath("$.title").value("Ebéd utáni meeting"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createBookingRoomNotFoundTest() throws Exception {
        //Given
        OffsetDateTime from = OffsetDateTime.parse("2026-03-06T12:00:00Z");
        OffsetDateTime to = OffsetDateTime.parse("2026-03-06T13:00:00Z");

        Booking invalidRequest = new Booking(999L, from, to);
        invalidRequest.setTitle("Invalid room");

        when(service.createBooking(any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Tárgyaló nem található: 999"));
        // When/Then
        mockMvc.perform(post("/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void getAllBookingsTest() throws Exception {
        // Given
        when(service.getBookings(null, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getFilteredBookingsTest() throws Exception {
        // Given
        when(service.getBookings(1L, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/v1/bookings")
                        .param("roomId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookingTest() throws Exception {
        // Given - service deleteById hívása tesztelése
        doNothing().when(service).deleteBooking(1L);

        // When/Then
        mockMvc.perform(delete("/v1/bookings/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteBooking(1L);
    }

    @Test
    void deleteBookingNotFoundTest() throws Exception {
        // Given
        doThrow(new BookingNotFoundException("Foglalás nem található: 999"))
                .when(service).deleteBooking(999L);

        // When/Then
        mockMvc.perform(delete("/v1/bookings/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Foglalás nem található: 999"));
    }
}