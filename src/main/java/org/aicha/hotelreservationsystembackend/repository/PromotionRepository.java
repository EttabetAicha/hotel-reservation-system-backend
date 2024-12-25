package org.aicha.hotelreservationsystembackend.repository;

import org.aicha.hotelreservationsystembackend.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    List<Promotion> findByHotelId(UUID hotelId);
    List<Promotion> findByStartDateBeforeAndEndDateAfter(LocalDateTime now, LocalDateTime now2);
    List<Promotion> findByDiscountPercentageGreaterThan(double percentage);
}
