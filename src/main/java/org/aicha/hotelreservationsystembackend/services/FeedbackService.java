package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Feedback;
import org.aicha.hotelreservationsystembackend.repository.FeedbackRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        try {
            return feedbackRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all feedbacks", e);
        }
    }

    public Feedback getFeedbackById(UUID id) {
        try {
            return feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedback by ID", e);
        }
    }

    public Feedback createFeedback(Feedback feedback) {
        try {
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Error creating feedback", e);
        }
    }

    public Feedback updateFeedback(UUID id, Feedback feedbackDetails) {
        try {
            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for this id :: " + id));

            feedback.setComment(feedbackDetails.getComment());
            feedback.setRating(feedbackDetails.getRating());
            feedback.setUser(feedbackDetails.getUser());
            feedback.setHotel(feedbackDetails.getHotel());

            return feedbackRepository.save(feedback);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating feedback", e);
        }
    }

    public void deleteFeedback(UUID id) {
        try {
            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for this id :: " + id));

            feedbackRepository.delete(feedback);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting feedback", e);
        }
    }
}