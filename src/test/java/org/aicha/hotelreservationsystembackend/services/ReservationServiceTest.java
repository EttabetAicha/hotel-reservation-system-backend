package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.domain.User;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;
import org.aicha.hotelreservationsystembackend.domain.enums.ReservationStatus;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.aicha.hotelreservationsystembackend.mapper.PaymentMapper;
import org.aicha.hotelreservationsystembackend.mapper.ReservationMapper;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    private UUID reservationId;
    private UUID roomId;
    private UUID clientId;
    private UUID hotelId;
    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        reservationId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        clientId = UUID.randomUUID();
        hotelId = UUID.randomUUID();

        payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentStatus(PaymentStatus.PENDING);

        Room room = new Room();
        room.setId(roomId);

        User client = new User();
        client.setId(clientId);

        reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setRoom(room);
        reservation.setClient(client);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCheckIn(LocalDateTime.now().plusDays(1));
        reservation.setCheckOut(LocalDateTime.now().plusDays(5));
        reservation.setPayment(payment);

        reservationDTO = new ReservationDTO();
        reservationDTO.setRoomId(roomId);
        reservationDTO.setClientId(clientId);
        reservationDTO.setCheckIn(reservation.getCheckIn());
        reservationDTO.setCheckOut(reservation.getCheckOut());
        reservationDTO.setPayment(paymentDTO);
    }

    @Test
    void getReservationById_ShouldReturnReservation_WhenFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.getReservationById(reservationId);
        assertNotNull(result);
        assertEquals(reservationDTO.getRoomId(), result.getRoomId());
    }

    @Test
    void getReservationById_ShouldThrowException_WhenNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(reservationId));
    }


    @Test
    void createReservationWithPayment_ShouldSaveAndReturnReservation() {
        when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
        when(reservationMapper.toEntity(reservationDTO)).thenReturn(reservation);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.createReservationWithPayment(reservationDTO);
        assertNotNull(result);
    }

    @Test
    void updateReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.updateReservation(reservationId, reservationDTO));
    }

    @Test
    void deleteReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.deleteReservation(reservationId));
    }

    @Test
    void deleteReservation_ShouldDelete_WhenReservationExists() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).delete(reservation);

        assertDoesNotThrow(() -> reservationService.deleteReservation(reservationId));
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void getReservationsByHotelId_ShouldReturnReservations() {
        when(reservationRepository.findByRoomHotelId(hotelId)).thenReturn(Arrays.asList(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        List<ReservationDTO> results = reservationService.getReservationsByHotelId(hotelId);
        assertFalse(results.isEmpty());
    }
}
