package org.aicha.hotelreservationsystembackend.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private UUID id;
    private UUID reservationId;
    private Double amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String stripePaymentIntentId;
}
