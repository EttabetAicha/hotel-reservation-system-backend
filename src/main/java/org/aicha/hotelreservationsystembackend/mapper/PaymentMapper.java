package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "reservation.id", target = "reservationId")
    PaymentDTO toDTO(Payment payment);

    @Mapping(source = "reservationId", target = "reservation.id")
    Payment toEntity(PaymentDTO paymentDTO);

    @Mapping(source = "reservationId", target = "reservation.id")
    void updatePaymentFromDTO(PaymentDTO paymentDTO, @MappingTarget Payment payment);
}
