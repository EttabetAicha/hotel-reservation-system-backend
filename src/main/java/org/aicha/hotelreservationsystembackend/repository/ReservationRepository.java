package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUserId(UUID userId);
    List<Reservation> findByHotelId(UUID hotelId);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByCheckInDateBetween(LocalDateTime startDate, Date endDate);
    List<Reservation> findByRoomId(UUID roomId);
}