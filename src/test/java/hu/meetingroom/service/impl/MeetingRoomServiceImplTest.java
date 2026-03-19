package hu.meetingroom.service.impl;

import com.example.meetingroom.model.MeetingRoom;
import hu.meetingroom.mapper.MeetingRoomMapper;
import hu.meetingroom.repository.MeetingRoomRepository;
import hu.meetingroom.service.MeetingRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MeetingRoomServiceImplTest {

    @Mock
    private MeetingRoomRepository repository;

    @Mock
    private MeetingRoomMapper mapper;

    @InjectMocks
    private MeetingRoomServiceImpl service;

    private List<MeetingRoom> expectedRooms;

    @BeforeEach
    void setUp() {
        MeetingRoom bigRoom = new MeetingRoom(1L, "Nagy tárgyaló");
        bigRoom.setLocation("2. emelet");
        bigRoom.setCapacity(10);
        MeetingRoom smallRooom = new MeetingRoom(2L, "Kis tárgyaló");
        smallRooom.setLocation("1. emelet");
        smallRooom.setCapacity(6);
        expectedRooms = List.of(bigRoom, smallRooom);
    }

    @Test
    void getAllRooms() {
        // Given
        when(repository.findAll()).thenReturn(List.of());
        when(mapper.toDtoList(any())).thenReturn(expectedRooms);

        // When
        List<MeetingRoom> result = service.getAllRooms();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Nagy tárgyaló");
    }
}