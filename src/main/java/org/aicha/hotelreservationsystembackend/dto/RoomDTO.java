package org.aicha.hotelreservationsystembackend.dto;

import lombok.Data;
import org.aicha.hotelreservationsystembackend.domain.enums.RoomType;

import java.util.UUID;

@Data
public class RoomDTO {
    private UUID id;
    private String name;
    private String roomNumber;
    private RoomType type;
    private Double price;
    private Boolean isAvailable;
    private String description;
}