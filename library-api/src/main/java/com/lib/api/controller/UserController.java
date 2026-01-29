package com.lib.api.controller;

import com.lib.api.model.User;
import com.lib.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * User/Librarian Task: View own profile details.
     * Identifies user via the JWT token (Principal).
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Principal principal) {
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    /**
     * Librarian Task: View list of all members.
     */
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Librarian Task: Blacklist a member.
     */
    @PutMapping("/{id}/blacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<User> blacklistUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.blacklistUser(id));
    }

    /**
     * Librarian Task: Permanently remove a user record.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}