package com.lib.api.controller;

import com.lib.api.dto.ReservationRequest;
import com.lib.api.dto.ReservationResponse;
import com.lib.api.model.Reservation;
import com.lib.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // Anyone authenticated can reserve
    @PostMapping
    public ResponseEntity<ReservationResponse> reserveBook(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    // Anyone authenticated can return
    @PutMapping("/{id}/return")
    public ResponseEntity<ReservationResponse> returnBook(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.returnBook(id));
    }

    // STANDARD WAY: Librarian only can see all reservations
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // STANDARD WAY: Librarian only can delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
