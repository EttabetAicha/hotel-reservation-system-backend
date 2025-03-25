package org.aicha.hotelreservationsystembackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReservationDTO {

    private UUID id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private UUID clientId;
    private UUID roomId;
    private PaymentDTO payment;
    private double totalPrice;

    @NotNull
    private ReservationStatus status;
}