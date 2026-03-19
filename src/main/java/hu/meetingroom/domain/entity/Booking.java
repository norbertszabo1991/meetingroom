package hu.meetingroom.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Column(name = "room_id")
    private Long roomId;

    @NotNull
    @Future(message = "Kezdés nem lehet múltban")
    @Column(name = "\"from\"")
    private LocalDateTime from;

    @NotNull
    @Future(message = "Befejezés nem lehet múltban")
    @Column(name = "\"to\"")
    private LocalDateTime to;

    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors, Getters/Setters
    public Booking() {}

    public Booking(Long roomId, LocalDateTime from, LocalDateTime to, String title) {
        this.roomId = roomId;
        this.from = from;
        this.to = to;
        this.title = title;
    }

    // Getters/Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public LocalDateTime getFrom() { return from; }
    public void setFrom(LocalDateTime from) { this.from = from; }
    public LocalDateTime getTo() { return to; }
    public void setTo(LocalDateTime to) { this.to = to; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
