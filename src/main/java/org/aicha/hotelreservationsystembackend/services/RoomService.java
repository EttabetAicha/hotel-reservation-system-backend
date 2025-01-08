package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.repository.RoomRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        try {
            return roomRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all rooms", e);
        }
    }

    public Room getRoomById(UUID id) {
        try {
            return roomRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving room by ID", e);
        }
    }

    public Room createRoom(Room room) {
        try {
            return roomRepository.save(room);
        } catch (Exception e) {
            throw new RuntimeException("Error creating room", e);
        }
    }

    public Room updateRoom(UUID id, Room roomDetails) {
        try {
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));

            room.setName(roomDetails.getName());
            room.setType(roomDetails.getType());
            room.setPrice(roomDetails.getPrice());
            room.setHotel(roomDetails.getHotel());

            return roomRepository.save(room);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating room", e);
        }
    }

    public void deleteRoom(UUID id) {
        try {
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));

            roomRepository.delete(room);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting room", e);
        }
    }
}