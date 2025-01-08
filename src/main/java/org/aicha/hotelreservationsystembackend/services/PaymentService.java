package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.repository.PaymentRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        try {
            return paymentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all payments", e);
        }
    }

    public Payment getPaymentById(UUID id) {
        try {
            return paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving payment by ID", e);
        }
    }

    public Payment createPayment(Payment payment) {
        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Error creating payment", e);
        }
    }

    public Payment updatePayment(UUID id, Payment paymentDetails) {
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for this id :: " + id));

            payment.setAmount(paymentDetails.getAmount());
            payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            payment.setPaymentStatus(paymentDetails.getPaymentStatus());
            payment.setReservation(paymentDetails.getReservation());

            return paymentRepository.save(payment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating payment", e);
        }
    }

    public void deletePayment(UUID id) {
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for this id :: " + id));

            paymentRepository.delete(payment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting payment", e);
        }
    }
}