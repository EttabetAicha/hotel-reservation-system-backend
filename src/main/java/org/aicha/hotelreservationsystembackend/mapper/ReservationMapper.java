package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface ReservationMapper {

    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "roomId", source = "room.id")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "room.id", source = "roomId")
    Reservation toEntity(ReservationDTO reservationDTO);
    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "room.id", source = "roomId")
    void updateReservationFromDTO(ReservationDTO reservationDTO, @MappingTarget Reservation reservation);

}