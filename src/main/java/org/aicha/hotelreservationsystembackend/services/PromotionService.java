package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Promotion;
import org.aicha.hotelreservationsystembackend.repository.PromotionRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> getAllPromotions() {
        try {
            return promotionRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all promotions", e);
        }
    }

    public Promotion getPromotionById(UUID id) {
        try {
            return promotionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving promotion by ID", e);
        }
    }

    public Promotion createPromotion(Promotion promotion) {
        try {
            return promotionRepository.save(promotion);
        } catch (Exception e) {
            throw new RuntimeException("Error creating promotion", e);
        }
    }

    public Promotion updatePromotion(UUID id, Promotion promotionDetails) {
        try {
            Promotion promotion = promotionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found for this id :: " + id));

            promotion.setTitle(promotionDetails.getTitle());
            promotion.setDescription(promotionDetails.getDescription());
            promotion.setStartDate(promotionDetails.getStartDate());
            promotion.setEndDate(promotionDetails.getEndDate());

            return promotionRepository.save(promotion);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating promotion", e);
        }
    }

    public void deletePromotion(UUID id) {
        try {
            Promotion promotion = promotionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found for this id :: " + id));

            promotionRepository.delete(promotion);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting promotion", e);
        }
    }
}