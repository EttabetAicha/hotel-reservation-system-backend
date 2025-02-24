package org.aicha.hotelreservationsystembackend.mapper;


import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.domain.User;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.aicha.hotelreservationsystembackend.repository.RoomRepository;
import org.aicha.hotelreservationsystembackend.repository.UserRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReservationMapper {

    @Autowired
    protected UserRepository clientRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "room.id", target = "roomId")
    public abstract ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "client", expression = "java(getClientById(dto.getClientId()))")
    @Mapping(target = "room", expression = "java(getRoomById(dto.getRoomId()))")
    public abstract Reservation toEntity(ReservationDTO dto);

    protected User getClientById(UUID clientId) {
        if (clientId == null) {
            return null;
        }
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
    }

    protected Room getRoomById(UUID roomId) {
        if (roomId == null) {
            return null;
        }
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
    }

    @AfterMapping
    protected void setPaymentReservation(@MappingTarget Reservation reservation) {
        if (reservation.getPayment() != null) {
            reservation.getPayment().setReservation(reservation);
        }
    }
}