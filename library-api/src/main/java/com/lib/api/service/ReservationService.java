package com.lib.api.service;

import com.lib.api.dto.BookResponse;
import com.lib.api.dto.ReservationRequest;
import com.lib.api.dto.ReservationResponse;
import com.lib.api.dto.UserResponse;
import com.lib.api.model.*;
import com.lib.api.repository.BookRepository;
import com.lib.api.repository.ReservationRepository;
import com.lib.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        // 1. Fetch and Validate (Existing logic) [cite: 226]
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available");
        }

        // 2. Save Reservation [cite: 16]
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(request.getDays()));
        reservation.setStatus(ReservationStatus.ACTIVE);

        book.setStatus(BookStatus.RESERVED);
        bookRepository.save(book);
        Reservation savedReservation = reservationRepository.save(reservation);

        // 3. RETURN THE DTO INSTEAD OF THE ENTITY
        return mapToResponse(savedReservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        // Map User to safe UserResponse [cite: 34]
        UserResponse userResp = new UserResponse();
        userResp.setId(reservation.getUser().getId());
        userResp.setEmail(reservation.getUser().getEmail());
        userResp.setRole(reservation.getUser().getRole());

        // Map Book to safe BookResponse
        BookResponse bookResp = BookResponse.builder()
                .id(reservation.getBook().getId())
                .title(reservation.getBook().getTitle())
                .author(reservation.getBook().getAuthor())
                .isbn(reservation.getBook().getIsbn())
                .build();

        // Build the final ReservationResponse
        return ReservationResponse.builder()
                .id(reservation.getId())
                .user(userResp)
                .book(bookResp)
                .reservationDate(reservation.getReservationDate())
                .dueDate(reservation.getDueDate())
                .status(reservation.getStatus())
                .build();
    }
}