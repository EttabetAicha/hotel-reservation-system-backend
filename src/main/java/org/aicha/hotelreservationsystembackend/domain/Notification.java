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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String message;
    @ManyToOne
    private User user;
    private LocalDateTime date;
    public void send() {}


}
