package org.aicha.hotelreservationsystembackend.web.rest;

import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.aicha.hotelreservationsystembackend.dto.HotelDTO;
import org.aicha.hotelreservationsystembackend.services.HotelService;
import org.aicha.hotelreservationsystembackend.mapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    public HotelController(HotelService hotelService, HotelMapper hotelMapper) {
        this.hotelService = hotelService;
        this.hotelMapper = hotelMapper;
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestBody HotelDTO hotelDTO) {
        Hotel createdHotel = hotelService.createHotel(hotelDTO);
        return ResponseEntity.ok(hotelMapper.hotelToHotelDTO(createdHotel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable UUID id) {
        Hotel hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotelMapper.hotelToHotelDTO(hotel));
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels.stream().map(hotelMapper::hotelToHotelDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable UUID id, @RequestBody HotelDTO hotelDTO) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotelMapper.hotelDTOToHotel(hotelDTO));
        return ResponseEntity.ok(hotelMapper.hotelToHotelDTO(updatedHotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
