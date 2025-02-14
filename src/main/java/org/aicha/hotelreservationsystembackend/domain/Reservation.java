package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
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
    private ReservationStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    @ManyToOne
    private Payment payment;


}