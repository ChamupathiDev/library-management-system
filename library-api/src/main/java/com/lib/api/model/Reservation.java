package com.lib.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // [cite: 190]

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // [cite: 190]

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // [cite: 190]

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate; // [cite: 190]

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate; // [cite: 190]

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.ACTIVE; // [cite: 190]
}
