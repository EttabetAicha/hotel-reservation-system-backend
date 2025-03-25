package org.aicha.hotelreservationsystembackend.dto;

import lombok.Data;
import org.aicha.hotelreservationsystembackend.domain.enums.RoomStatus;
import org.aicha.hotelreservationsystembackend.domain.enums.RoomType;

import java.util.UUID;

@Data
public class RoomDTO {
    private UUID id;
    private String name;
    private UUID hotel;
    private String roomNumber;
    private RoomType type;
    private Double price;
    private Boolean isAvailable;
    private String description;
    private String imageUrl;
    private RoomStatus status;
    private String hotelName;
}