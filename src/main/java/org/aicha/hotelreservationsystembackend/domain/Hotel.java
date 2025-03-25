package org.aicha.hotelreservationsystembackend.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.aicha.hotelreservationsystembackend.config.UserDeserializer;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private Integer rating;
    private Integer stars;
    private String status;
    @ElementCollection
    private List<String> amenities;
    @ElementCollection
    private List<String> images;
    @ManyToOne
    @JsonDeserialize(using = UserDeserializer.class)
    private User owner;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Room> rooms;

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setHotel(null);
    }

    public void updateDetails(String name, String description, String address, String location, String city, String country, Integer rating, Integer stars, String status, List<String> amenities, List<String> images) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.rating = rating;
        this.stars = stars;
        this.status = status;
        this.amenities = amenities;
        this.images = images;
    }
}