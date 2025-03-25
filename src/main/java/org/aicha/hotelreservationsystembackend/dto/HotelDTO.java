package org.aicha.hotelreservationsystembackend.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.aicha.hotelreservationsystembackend.config.UserDeserializer;
import org.aicha.hotelreservationsystembackend.domain.User;

import java.util.List;
import java.util.UUID;

@Data
public class HotelDTO {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private Integer rating;
    private Integer stars;
    private String status;
    private List<String> amenities;
    private List<String> images;
    private UUID OwnerId;
}