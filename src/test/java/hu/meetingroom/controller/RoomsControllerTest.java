package hu.meetingroom.controller;

import com.example.meetingroom.model.MeetingRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.meetingroom.service.MeetingRoomService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(RoomsController.class)
class RoomsControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeetingRoomService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRoomsTest() throws Exception {
        // Given
        MeetingRoom room1 = new MeetingRoom();
        room1.setId(1L);
        room1.setName("Nagy tárgyaló");
        room1.setLocation("2. emelet");
        room1.setCapacity(10);
        MeetingRoom room2 = new MeetingRoom();
        room2.setId(2L);
        room2.setName("Kis tárgyaló");
        room2.setCapacity(6);
        room2.setLocation("1. emelet");
        when(service.getAllRooms()).thenReturn(List.of(room1, room2));

        // When/Then
        mockMvc.perform(get("/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Nagy tárgyaló"))
                .andExpect(jsonPath("$[1].capacity").value(6));
    }
}