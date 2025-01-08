package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByHotelId(UUID hotelId);
    List<Room> findByIsAvailable(boolean isAvailable);
    List<Room> findByPriceLessThanEqual(double price);
}