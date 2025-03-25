package org.aicha.hotelreservationsystembackend.web.rest;

import org.aicha.hotelreservationsystembackend.dto.PaymentDTO;
import org.aicha.hotelreservationsystembackend.dto.ReservationDTO;
import org.aicha.hotelreservationsystembackend.services.ReservationService;
import org.aicha.hotelreservationsystembackend.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationWithPaymentDTO) {
        try {
            ReservationDTO createdReservation = reservationService.createReservationWithPayment(reservationWithPaymentDTO);
            return ResponseEntity.ok(createdReservation);
        } catch (Exception e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable UUID id) {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable UUID id, @RequestBody ReservationDTO reservationDetails) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDetails);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClientId(@PathVariable UUID clientId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByHotelId(@PathVariable UUID hotelId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByHotelId(hotelId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable String status) {
        List<ReservationDTO> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/checkin")
    public ResponseEntity<List<ReservationDTO>> getReservationsByCheckInDateBetween(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        List<ReservationDTO> reservations = reservationService.getReservationsByCheckInDateBetween(startDate, endDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByRoomId(@PathVariable UUID roomId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByRoomId(roomId);
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/room/{id}/name")
    public ResponseEntity<String> getRoomNameById(@PathVariable UUID id) {
        String roomName = reservationService.getRoomNameById(id);
        return ResponseEntity.ok(roomName);
    }

    @GetMapping("/room/{id}/hotel-name")
    public ResponseEntity<String> getHotelNameByRoomId(@PathVariable UUID id) {
        String hotelName = reservationService.getHotelNameByRoomId(id);
        return ResponseEntity.ok(hotelName);
    }

    @GetMapping("/room/{roomId}/hotel-images")
    public ResponseEntity<?> getHotelImageByRoomId(@PathVariable UUID roomId) {
        try {
            String imageUrl = reservationService.getHotelImagesByRoomId(roomId);

            Map<String, String> response = new HashMap<>();
            response.put("images", imageUrl);
            return ResponseEntity.ok(response);


        } catch (Exception e) {
            return ResponseEntity.ok("assets/default-hotel.jpg");
        }
    }
}