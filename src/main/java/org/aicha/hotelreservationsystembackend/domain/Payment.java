package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;

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
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private String paymentMethod;
    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;


    public boolean processPayment() {

        return false;
    }


}