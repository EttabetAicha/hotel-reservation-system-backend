package org.aicha.hotelreservationsystembackend.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class HotelDTO {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private String location;
    private String city;
    private String country;
    private Integer rating;
    private List<String> images;
    private UUID ownerId;
}