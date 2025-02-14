package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByClientId(UUID clientId);
    List<Reservation> findByRoomHotelId(UUID hotelId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCheckInBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Reservation> findByRoomId(UUID roomId);
}