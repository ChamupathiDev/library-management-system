package com.lib.api.dto;

import com.lib.api.model.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationResponse {
    private Integer id;
    private UserResponse user; // Uses your safe User DTO
    private BookResponse book; // We will define this simple DTO below
    private LocalDate reservationDate;
    private LocalDate dueDate;
    private ReservationStatus status;
}
