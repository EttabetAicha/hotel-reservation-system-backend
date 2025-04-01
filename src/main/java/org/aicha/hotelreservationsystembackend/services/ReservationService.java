package org.aicha.hotelreservationsystembackend.services;

import jakarta.validation.Valid;
import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.aicha.hotelreservationsystembackend.mapper.PaymentMapper;
import org.aicha.hotelreservationsystembackend.mapper.ReservationMapper;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private  PaymentMapper paymentMapper;

    public List<ReservationDTO> getReservationsByClientId(UUID clientId) {
        try {
            List<Reservation> reservations = reservationRepository.findByClientId(clientId);
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by client ID", e);
        }
    }

    public List<ReservationDTO> getReservationsByHotelId(UUID hotelId) {
        try {
            List<Reservation> reservations = reservationRepository.findByRoomHotelId(hotelId);
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by hotel ID", e);
        }
    }

    public List<ReservationDTO> getReservationsByStatus(String status) {
        try {
            List<Reservation> reservations = reservationRepository.findByStatus(ReservationStatus.valueOf(status));
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by status", e);
        }
    }

    public List<ReservationDTO> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all reservations", e);
        }
    }

    public List<ReservationDTO> getReservationsByCheckInDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Reservation> reservations = reservationRepository.findByCheckInBetween(startDate, endDate);
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by check-in date range", e);
        }
    }

    public List<ReservationDTO> getReservationsByRoomId(UUID roomId) {
        try {
            List<Reservation> reservations = reservationRepository.findByRoomId(roomId);
            return reservations.stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservations by room ID", e);
        }
    }

    public ReservationDTO getReservationById(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + id));
            return reservationMapper.toDTO(reservation);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservation by ID", e);
        }
    }
    private void validateNoOverlappingReservations(UUID roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(roomId, checkIn, checkOut);
        if (!overlappingReservations.isEmpty()) {
            throw new IllegalArgumentException("The room is already booked for the selected dates.");
        }
    }
    @Transactional
    public ReservationDTO createReservationWithPayment(ReservationDTO reservationWithPaymentDTO) {
        validateReservationInput(reservationWithPaymentDTO);
        validateNoOverlappingReservations(reservationWithPaymentDTO.getRoomId(), reservationWithPaymentDTO.getCheckIn(), reservationWithPaymentDTO.getCheckOut());
        try {
            PaymentDTO paymentDTO = reservationWithPaymentDTO.getPayment();
            if (paymentDTO == null) {
                throw new IllegalArgumentException("Payment details must be provided");
            }

            paymentDTO.setPaymentStatus(PaymentStatus.PENDING);

            Payment paymentEntity = paymentMapper.toEntity(paymentDTO);

            Reservation reservation = reservationMapper.toEntity(reservationWithPaymentDTO);

            reservation.setStatus(ReservationStatus.PENDING);

            reservation.setPayment(paymentEntity);
            paymentEntity.setReservation(reservation);

            Payment savedPayment = paymentRepository.save(paymentEntity);

            reservation.setPayment(savedPayment);
            Reservation savedReservation = reservationRepository.save(reservation);

            return reservationMapper.toDTO(savedReservation);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create reservation with payment", e);
        }
    }

    private void validateReservationInput(ReservationDTO reservationDTO) {
        if (reservationDTO.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID must not be null");
        }
        if (reservationDTO.getClientId() == null) {
            throw new IllegalArgumentException("Client ID must not be null");
        }
        if (reservationDTO.getCheckIn() == null || reservationDTO.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and Check-out dates must not be null");
        }
        if (reservationDTO.getCheckIn().isAfter(reservationDTO.getCheckOut())) {
            throw new IllegalArgumentException("Check-in date must be before Check-out date");
        }
    }


    public ReservationDTO updateReservation(UUID id, @Valid ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + id));
        reservationMapper.updateReservationFromDTO(reservationDTO, reservation);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updatedReservation);
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

    public String getRoomNameById(UUID id) {
        return reservationRepository.findRoomNameById(id);
    }

    public String getHotelNameByRoomId(UUID id) {
        return reservationRepository.findHotelNameByRoomId(id);
    }

    public String getHotelImagesByRoomId(UUID id) {
        return reservationRepository.findRoomImagesByRoomId(id);
    }



}