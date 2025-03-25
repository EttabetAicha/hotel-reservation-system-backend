package org.aicha.hotelreservationsystembackend.dto;

import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private UUID id;
    private UUID reservationId;
    private double amount;
    private String paymentMethod;
    private String stripePaymentIntentId;
    private PaymentStatus paymentStatus;
    private String payerName;
    private String payerEmail;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;
    private LocalDateTime paymentDate;
    private LocalDateTime lastUpdated;
}