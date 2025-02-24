package org.aicha.hotelreservationsystembackend.web.rest;

import org.aicha.hotelreservationsystembackend.domain.Payment;
import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<PaymentDTO> confirmPayment(@PathVariable String paymentIntentId) {
        PaymentDTO confirmedPayment = paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok(confirmedPayment);
    }

    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable String paymentIntentId) {
        PaymentDTO cancelledPayment = paymentService.cancelPayment(paymentIntentId);
        return ResponseEntity.ok(cancelledPayment);
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