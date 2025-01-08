package org.aicha.hotelreservationsystembackend.services;

import org.aicha.hotelreservationsystembackend.domain.Notification;
import org.aicha.hotelreservationsystembackend.repository.NotificationRepository;
import org.aicha.hotelreservationsystembackend.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        try {
            return notificationRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all notifications", e);
        }
    }

    public Notification getNotificationById(UUID id) {
        try {
            return notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this id :: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving notification by ID", e);
        }
    }

    public Notification createNotification(Notification notification) {
        try {
            return notificationRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException("Error creating notification", e);
        }
    }

    public Notification updateNotification(UUID id, Notification notificationDetails) {
        try {
            Notification notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this id :: " + id));

            notification.setMessage(notificationDetails.getMessage());
            notification.setUser(notificationDetails.getUser());
            notification.setDate(notificationDetails.getDate());

            return notificationRepository.save(notification);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating notification", e);
        }
    }

    public void deleteNotification(UUID id) {
        try {
            Notification notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this id :: " + id));

            notificationRepository.delete(notification);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting notification", e);
        }
    }
}