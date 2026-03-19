package hu.meetingroom.service.impl;

import com.example.meetingroom.model.MeetingRoom;
import hu.meetingroom.mapper.MeetingRoomMapper;
import hu.meetingroom.repository.MeetingRoomRepository;
import hu.meetingroom.service.MeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    private final MeetingRoomRepository repository;
    private final MeetingRoomMapper mapper;

    public MeetingRoomServiceImpl(MeetingRoomRepository repository, MeetingRoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<MeetingRoom> getAllRooms() {
        return mapper.toDtoList(repository.findAll());
    }
}
