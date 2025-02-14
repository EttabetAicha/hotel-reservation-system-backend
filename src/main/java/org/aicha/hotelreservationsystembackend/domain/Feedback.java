package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    @ManyToOne
    private User user;
    @ManyToOne
    private Hotel hotel;

    public void submitFeedback() {}



}
