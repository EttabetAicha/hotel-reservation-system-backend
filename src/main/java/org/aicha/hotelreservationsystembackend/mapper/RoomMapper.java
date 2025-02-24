package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.dto.RoomDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPrice(room.getPrice());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setDescription(room.getDescription());
        return dto;
    }
}