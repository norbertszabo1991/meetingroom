package hu.meetingroom.mapper;

import hu.meetingroom.domain.entity.MeetingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MeetingRoomMapper {

    com.example.meetingroom.model.MeetingRoom toDto(MeetingRoom entity);

    List< com.example.meetingroom.model.MeetingRoom> toDtoList(Iterable<MeetingRoom> entities);
}
