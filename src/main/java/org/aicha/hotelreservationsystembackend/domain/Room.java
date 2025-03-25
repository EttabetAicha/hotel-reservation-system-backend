package org.aicha.hotelreservationsystembackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.RoomStatus;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Hotel hotel;
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private Double price;
    private Boolean isAvailable;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    public String getHotelName() {
        return this.hotel != null ? this.hotel.getName() : null;
    }
    @Transient
    private String hotelName;

    @PostLoad
    public void setHotelName() {
        this.hotelName = this.hotel != null ? this.hotel.getName() : null;
    }


    public void updateAvailability(boolean status) {}
    public void updatePrice(double price) {}
}