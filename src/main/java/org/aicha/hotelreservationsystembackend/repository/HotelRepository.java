package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    Optional<Hotel> findByName(String name);
}
