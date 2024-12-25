package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByHotelId(Integer hotelId);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByCheckInDateBetween(LocalDateTime startDate, Date endDate);
    List<Reservation> findByRoomId(Integer roomId);
}