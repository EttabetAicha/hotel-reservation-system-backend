package org.aicha.hotelreservationsystembackend.services;

import jakarta.validation.Valid;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getReservationsByUserId(UUID userId) {
        try {
            return reservationRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by user ID", e);
        }
    }

    public List<Reservation> getReservationsByHotelId(UUID hotelId) {
        try {
            return reservationRepository.findByHotelId(hotelId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by hotel ID", e);
        }
    }

    public List<Reservation> getReservationsByStatus(String status) {
        try {
            return reservationRepository.findByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by status", e);
        }
    }

    public List<Reservation> getReservationsByCheckInDateBetween(LocalDateTime startDate, Date endDate) {
        try {
            return reservationRepository.findByCheckInDateBetween(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by check-in date range", e);
        }
    }

    public List<Reservation> getReservationsByRoomId(UUID roomId) {
        try {
            return reservationRepository.findByRoomId(roomId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by room ID", e);
        }
    }

    public Reservation getReservationById(UUID id) {
        try {
            return reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservation by ID", e);
        }
    }

    public Reservation createReservation(@Valid Reservation reservation) {
        try {
            return reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating reservation", e);
        }
    }

    public Reservation updateReservation(UUID id, @Valid Reservation reservationDetails) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + id));

            reservation.setCheckIn(reservationDetails.getCheckIn());
            reservation.setCheckOut(reservationDetails.getCheckOut());
            reservation.setStatus(reservationDetails.getStatus());
            reservation.setRoom(reservationDetails.getRoom());
            reservation.setClient(reservationDetails.getClient());
            reservation.setTotalPrice(reservationDetails.getTotalPrice());
            reservation.setCreatedAt(reservationDetails.getCreatedAt());
            reservation.setPayment(reservationDetails.getPayment());

            return reservationRepository.save(reservation);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating reservation", e);
        }
    }

    public void deleteReservation(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + id));

            reservationRepository.delete(reservation);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting reservation", e);
        }
    }
}