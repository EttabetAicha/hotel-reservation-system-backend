package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    private Reservation reservation;
    private Double amount;
    private String paymentMethod;
    private String paymentStatus;


    public boolean processPayment() { return false; }


}