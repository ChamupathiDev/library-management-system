package com.lib.api.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Integer bookId;
    private int days; // As per PDF: 7, 14, or 21 days [cite: 184]
}
