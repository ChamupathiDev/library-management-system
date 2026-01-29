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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Librarian Task: View all reservations in the system.
     */
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * User Task: View only their own reservation history.
     * Filtered by email extracted from the Security Principal.
     */
    public List<ReservationResponse> getMyReservations(String email) {
        return reservationRepository.findAll()
                .stream()
                .filter(r -> r.getUser().getEmail().equals(email))
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Librarian Task: Remove a record from the database.
     */
    @Transactional
    public void deleteReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // If deleting an ACTIVE reservation, we must make the book AVAILABLE again
        if (reservation.getStatus() == ReservationStatus.ACTIVE) {
            Book book = reservation.getBook();
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);
        }

        reservationRepository.delete(reservation);
    }

    /**
     * User Task: Create a new reservation.
     */
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available");
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(request.getDays()));
        reservation.setStatus(ReservationStatus.ACTIVE);

        book.setStatus(BookStatus.RESERVED);
        bookRepository.save(book);
        Reservation savedReservation = reservationRepository.save(reservation);

        return mapToResponse(savedReservation);
    }

    /**
     * User Task: Return a reserved book.
     */
    @Transactional
    public ReservationResponse returnBook(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.RETURNED) {
            throw new RuntimeException("This book has already been returned");
        }

        reservation.setStatus(ReservationStatus.RETURNED);

        Book book = reservation.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        bookRepository.save(book);
        Reservation saved = reservationRepository.save(reservation);

        return mapToResponse(saved);
    }

    /**
     * Helper Method: Converts Entity to Safe DTO.
     */
    private ReservationResponse mapToResponse(Reservation reservation) {
        UserResponse userResp = new UserResponse();
        userResp.setId(reservation.getUser().getId());
        userResp.setEmail(reservation.getUser().getEmail());
        userResp.setRole(reservation.getUser().getRole());

        BookResponse bookResp = BookResponse.builder()
                .id(reservation.getBook().getId())
                .title(reservation.getBook().getTitle())
                .author(reservation.getBook().getAuthor())
                .isbn(reservation.getBook().getIsbn())
                .build();

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