package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.dto.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "hotel.id", target = "hotel")
    @Mapping(source = "hotel.name", target = "hotelName")
    RoomDTO toRoomDTO(Room room);

    @Mapping(source = "hotel", target = "hotel.id" ,ignore = true)
    Room toRoom(RoomDTO roomDTO);
    @Mapping(source = "hotel", target = "hotel.id")
    void updateRoomFromDTO(RoomDTO roomDTO, @MappingTarget Room room);
}