package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
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
    private String location;
    private String city;
    private String country;
    private Integer rating;
    @ElementCollection
    private List<String> images;
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "hotel")
    @ToString.Exclude
    private List<Room> rooms;
    public void addRoom(Room room) {}
    public void updateDetails() {}

}
