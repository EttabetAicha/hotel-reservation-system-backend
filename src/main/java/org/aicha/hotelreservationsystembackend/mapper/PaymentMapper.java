package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    @Autowired
    private ReservationRepository reservationRepository;

    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());

        if (payment.getReservation() != null) {
            dto.setReservationId(payment.getReservation().getId());
        }

        return dto;
    }

    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());

        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository
                    .findById(dto.getReservationId())
                    .orElse(null);
            payment.setReservation(reservation);
        }

        return payment;
    }
}