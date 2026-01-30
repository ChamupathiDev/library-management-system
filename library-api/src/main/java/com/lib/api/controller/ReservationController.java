package com.lib.api.controller;

import com.lib.api.dto.ReservationRequest;
import com.lib.api.dto.ReservationResponse;
import com.lib.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserveBook(
            @RequestBody ReservationRequest request,
            Principal principal) {
        return ResponseEntity.ok(reservationService.createReservation(request, principal.getName()));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')") // Add this line
    public ResponseEntity<ReservationResponse> returnBook(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.returnBook(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // UPDATED: Now strictly for Librarians only
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(Principal principal) {
        return ResponseEntity.ok(reservationService.getMyReservations(principal.getName()));
    }
}