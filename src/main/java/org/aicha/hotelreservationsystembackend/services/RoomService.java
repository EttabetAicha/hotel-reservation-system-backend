package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.aicha.hotelreservationsystembackend.domain.Room;
import org.aicha.hotelreservationsystembackend.dto.RoomDTO;
import org.aicha.hotelreservationsystembackend.mapper.RoomMapper;

import org.aicha.hotelreservationsystembackend.repository.RoomRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    public RoomDTO createRoom(RoomDTO roomDTO, Hotel hotel) {
        Room room = roomMapper.toRoom(roomDTO);
        room.setHotel(hotel);
        Room savedRoom = roomRepository.save(room);
        return roomMapper.toRoomDTO(savedRoom);
    }

    public RoomDTO getRoomById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));
        return roomMapper.toRoomDTO(room);
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toRoomDTO)
                .collect(Collectors.toList());
    }

    public RoomDTO updateRoom(UUID id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));
        roomMapper.updateRoomFromDTO(roomDTO, room);
        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toRoomDTO(updatedRoom);
    }
    public List<RoomDTO> getRoomDataByHotelId(UUID hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms.stream()
                .map(roomMapper::toRoomDTO)
                .collect(Collectors.toList());
    }

    public void deleteRoom(UUID id) {
        roomRepository.deleteById(id);
    }
}