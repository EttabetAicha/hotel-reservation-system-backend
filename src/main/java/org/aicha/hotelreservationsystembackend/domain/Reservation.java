package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;

import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    private User client;
    @ManyToOne
    private Room room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;
    private Double totalPrice;
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

}