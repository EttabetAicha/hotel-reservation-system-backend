package org.aicha.hotelreservationsystembackend.services;

import jakarta.persistence.EntityNotFoundException;
import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.aicha.hotelreservationsystembackend.domain.User;
import org.aicha.hotelreservationsystembackend.dto.HotelDTO;
import org.aicha.hotelreservationsystembackend.mapper.HotelMapper;
import org.aicha.hotelreservationsystembackend.repository.HotelRepository;
import org.aicha.hotelreservationsystembackend.repository.UserRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelMapper hotelMapper;

    public List<Hotel> getAllHotels() {
        try {
            return hotelRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all hotels", e);
        }
    }

    public Hotel getHotelById(UUID id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));
    }

    public Hotel createHotel(HotelDTO hotelDTO) {
        if (hotelDTO.getOwnerId() != null) {
            userRepository.findById(hotelDTO.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }
        Hotel hotel = hotelMapper.hotelDTOToHotel(hotelDTO);
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(UUID id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));

        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setCity(hotelDetails.getCity());
        hotel.setCountry(hotelDetails.getCountry());
        hotel.setRating(hotelDetails.getRating());
        hotel.setStars(hotelDetails.getStars());
        hotel.setStatus(hotelDetails.getStatus());
        hotel.setAmenities(hotelDetails.getAmenities());
        hotel.setImages(hotelDetails.getImages());

        return hotelRepository.save(hotel);
    }

    public void deleteHotel(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found for this id :: " + id));

        hotelRepository.delete(hotel);
    }
}
