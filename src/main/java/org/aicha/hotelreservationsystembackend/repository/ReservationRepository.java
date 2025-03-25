package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    @Query("SELECT r.name FROM Room r WHERE r.id = :id")
    String findRoomNameById(UUID id);
    @Query("SELECT r.hotel.name FROM Room r WHERE r.id = :id")
    String findHotelNameByRoomId(UUID id);
    @Query("SELECT r.imageUrl FROM Room r WHERE r.id = :id")
    String findRoomImagesByRoomId(UUID id);


}