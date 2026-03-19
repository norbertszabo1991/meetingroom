package hu.meetingroom.mapper;

import hu.meetingroom.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "from", source = "from", qualifiedByName = "offsetToLocal")
    @Mapping(target = "to", source = "to", qualifiedByName = "offsetToLocal")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "offsetToLocal")
    Booking toEntity(com.example.meetingroom.model.Booking dto);


    @Mapping(source = "from", target = "from", qualifiedByName = "localToOffset")
    @Mapping(source = "to", target = "to", qualifiedByName = "localToOffset")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localToOffset")
    com.example.meetingroom.model.Booking toDto(Booking entity);

    List<com.example.meetingroom.model.Booking> toDtoList(List<Booking> entities);

    // OFFSET → LOCAL konverter
    @Named("offsetToLocal")
    default LocalDateTime offsetToLocal(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
    }

    // LOCAL → OFFSET konverter (UTC)
    @Named("localToOffset")
    default OffsetDateTime localToOffset(LocalDateTime localDateTime) {
        return localDateTime != null ?
                localDateTime.atOffset(java.time.ZoneOffset.UTC) : null;
    }
}
