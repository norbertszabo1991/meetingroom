package hu.meetingroom.controller;

import com.example.meetingroom.model.MeetingRoom;
import hu.meetingroom.service.MeetingRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@Tag(name = "Rooms", description = "Meeting room operations")
public class RoomsController {
    private final MeetingRoomService service;

    public RoomsController(MeetingRoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MeetingRoom>> getRooms() {
        return ResponseEntity.ok(service.getAllRooms());
    }
}
