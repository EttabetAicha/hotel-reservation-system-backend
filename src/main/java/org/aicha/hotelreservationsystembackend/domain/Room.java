package org.aicha.hotelreservationsystembackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.RoomType;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String name;
    @ManyToOne
    @JsonIgnore
    private Hotel hotel;
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private Double price;
    private Boolean isAvailable;
    private String description;
    public void updateAvailability(boolean status) {}
    public void updatePrice(double price) {}


}