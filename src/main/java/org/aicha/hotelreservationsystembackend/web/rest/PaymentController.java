package org.aicha.hotelreservationsystembackend.web.rest;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable UUID id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        return ResponseEntity.ok(paymentDTO);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable UUID id, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<?> confirmPayment(@PathVariable String paymentIntentId) {
        if (!paymentIntentId.matches("pi_[a-zA-Z0-9]+_secret_[a-zA-Z0-9]+")) {
            return ResponseEntity.badRequest().body("Invalid payment intent ID format.");
        }
        try {
            PaymentDTO confirmedPayment = paymentService.confirmPayment(paymentIntentId);
            return ResponseEntity.ok(confirmedPayment);
        } catch (Exception e) {
            System.err.println("Error confirming payment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error confirming payment: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable String paymentIntentId) {
        PaymentDTO cancelledPayment = paymentService.cancelPayment(paymentIntentId);
        return ResponseEntity.ok(cancelledPayment);
    }
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestParam Double amount) {
        try {
            String paymentIntentId = paymentService.createStripePaymentIntent(amount);
            Map<String, String> response = new HashMap<>();
            response.put("paymentIntentId", paymentIntentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error creating payment intent: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creating payment intent: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByReservationId(@PathVariable UUID reservationId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByReservationId(reservationId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable String status) {
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
}