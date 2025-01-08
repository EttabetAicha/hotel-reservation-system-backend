package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.aicha.hotelreservationsystembackend.repository.HotelRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        try {
            return hotelRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all hotels", e);
        }
    }

    public Hotel getHotelById(UUID id) {
        try {
            return hotelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving hotel by ID", e);
        }
    }

    public Hotel createHotel(Hotel hotel) {
        try {
            return hotelRepository.save(hotel);
        } catch (Exception e) {
            throw new RuntimeException("Error creating hotel", e);
        }
    }

    public Hotel updateHotel(UUID id, Hotel hotelDetails) {
        try {
            Hotel hotel = hotelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));

            hotel.setName(hotelDetails.getName());
            hotel.setAddress(hotelDetails.getAddress());
            hotel.setRating(hotelDetails.getRating());

            return hotelRepository.save(hotel);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating hotel", e);
        }
    }

    public void deleteHotel(UUID id) {
        try {
            Hotel hotel = hotelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));

            hotelRepository.delete(hotel);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting hotel", e);
        }
    }
}