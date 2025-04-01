package org.aicha.hotelreservationsystembackend.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.mapper.PaymentMapper;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentService paymentService;

    private static final String TEST_STRIPE_API_KEY = "sk_test_51OgSUzCvwqwJehZs1ykmgRGJuhcNGh3SZ60JA5LIbO2053kyQsGxFAk308ADHEKeznRi4irGbgfhZY1vQr5TkOcS00FpGEut61";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentService = new PaymentService(
                paymentRepository,
                reservationRepository,
                paymentMapper,
                TEST_STRIPE_API_KEY
        );
    }



    @Test
    void testGetPaymentById() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(new PaymentDTO());

        PaymentDTO result = paymentService.getPaymentById(paymentId);

        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void testGetPaymentById_NotFound() {
        UUID paymentId = UUID.randomUUID();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(paymentId));
    }



    @Test
    void testUpdatePayment_PaymentNotFound() {
        UUID paymentId = UUID.randomUUID();
        PaymentDTO paymentDTO = new PaymentDTO();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.updatePayment(paymentId, paymentDTO));
    }



    @Test
    void testDeletePayment_PaymentNotFound() {
        UUID paymentId = UUID.randomUUID();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.deletePayment(paymentId));
        verify(paymentRepository, never()).delete(any(Payment.class));
    }


    @Test
    void testGetPaymentsByReservationId_NoPaymentsFound() {
        UUID reservationId = UUID.randomUUID();
        when(paymentRepository.findByReservationId(reservationId))
                .thenReturn(java.util.Collections.emptyList());

        List<PaymentDTO> results = paymentService.getPaymentsByReservationId(reservationId);

        assertTrue(results.isEmpty());
        verify(paymentMapper, never()).toDTO(any(Payment.class));
    }

    @Test
    void testGetPaymentsByStatus_InvalidStatus() {
        String invalidStatus = "INVALID_STATUS";

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.getPaymentsByStatus(invalidStatus));
    }


}