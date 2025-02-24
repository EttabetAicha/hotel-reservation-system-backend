package org.aicha.hotelreservationsystembackend.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.domain.Reservation;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;

import org.aicha.hotelreservationsystembackend.mapper.PaymentMapper;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.repository.ReservationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    public PaymentService(
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            PaymentMapper paymentMapper,
            @Value("${stripe.api.key}") String stripeApiKey) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.paymentMapper = paymentMapper;
        this.stripeApiKey = stripeApiKey;
        Stripe.apiKey = this.stripeApiKey;
    }


    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ID: " + id));
        return paymentMapper.toDTO(payment);
    }

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Reservation reservation = reservationRepository.findById(paymentDTO.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for ID: " + paymentDTO.getReservationId()));

        try {

            PaymentIntent paymentIntent = createStripePaymentIntent(paymentDTO.getAmount());
            Payment payment = paymentMapper.toEntity(paymentDTO);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setStripePaymentIntentId(paymentIntent.getId());

            return paymentMapper.toDTO(paymentRepository.save(payment));
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe payment intent", e);
        }
    }
    private PaymentIntent createStripePaymentIntent(Double amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }
    public PaymentDTO confirmPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for payment intent: " + paymentIntentId));

            if ("succeeded".equals(paymentIntent.getStatus())) {
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
            } else if ("canceled".equals(paymentIntent.getStatus())) {
                payment.setPaymentStatus(PaymentStatus.FAILED);
            }

            return paymentMapper.toDTO(paymentRepository.save(payment));
        } catch (StripeException e) {
            throw new RuntimeException("Failed to confirm payment", e);
        }
    }

    public PaymentDTO cancelPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent.cancel();

            Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for payment intent: " + paymentIntentId));

            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            return paymentMapper.toDTO(paymentRepository.save(payment));
        } catch (StripeException e) {
            throw new RuntimeException("Failed to cancel payment", e);
        }
    }

    public PaymentDTO updatePayment(UUID id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ID: " + id));

        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ID: " + id));

        paymentRepository.delete(payment);
    }
    public List<PaymentDTO> getPaymentsByReservationId(UUID reservationId) {
        return paymentRepository.findByReservationId(reservationId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<PaymentDTO> getPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(PaymentStatus.valueOf(status)).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void processPayment(Payment payment) {
        try {
            boolean isSuccessful = Math.random() > 0.2;

            if (isSuccessful) {
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
            } else {
                payment.setPaymentStatus(PaymentStatus.FAILED);
            }

            paymentRepository.save(payment);
        } catch (Exception e) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Error processing payment", e);
        }
    }


}
