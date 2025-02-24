package org.aicha.hotelreservationsystembackend.services;

import jakarta.validation.Valid;
import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.aicha.hotelreservationsystembackend.mapper.ReservationMapper;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService; // Assuming you have a PaymentService

    public List<Reservation> getReservationsByClientId(UUID clientId) {
        try {
            return reservationRepository.findByClientId(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by client ID", e);
        }
    }

    public List<Reservation> getReservationsByHotelId(UUID hotelId) {
        try {
            return reservationRepository.findByRoomHotelId(hotelId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by hotel ID", e);
        }
    }

    public List<Reservation> getReservationsByStatus(String status) {
        try {
            return reservationRepository.findByStatus(ReservationStatus.valueOf(status));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by status", e);
        }
    }

    public List<Reservation> getAllReservations() {
        try {
            return reservationRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all reservations", e);
        }
    }

    public List<Reservation> getReservationsByCheckInDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return reservationRepository.findByCheckInBetween(startDate, endDate);
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

    public ReservationDTO createReservation(@Valid ReservationDTO reservationDTO) {
        try {
            if (reservationDTO.getStatus() == null) {
                reservationDTO.setStatus(ReservationStatus.PENDING);
            }
            Reservation reservation = reservationMapper.toEntity(reservationDTO);
            Reservation savedReservation = reservationRepository.save(reservation);
            if (reservation.getPayment() != null) {
                Payment payment = new Payment();
                payment.setAmount(reservation.getPayment().getAmount());
                payment.setPaymentMethod(reservation.getPayment().getPaymentMethod());
                payment.setPaymentStatus(PaymentStatus.PENDING);
                Payment savedPayment = paymentRepository.save(payment);
                savedReservation.setPayment(savedPayment);
                savedPayment.setReservation(savedReservation);
                reservationRepository.save(savedReservation);
            }
            if (savedReservation.getPayment() != null) {
                paymentService.processPayment(savedReservation.getPayment());
            }

            return reservationMapper.toDTO(savedReservation);
        } catch (Exception e) {
            System.err.println("Detailed error creating reservation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating reservation: " + e.getMessage(), e);
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