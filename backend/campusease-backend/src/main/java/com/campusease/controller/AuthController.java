package com.campusease.controller;

import com.campusease.dto.UpdateRequest;
import com.campusease.exception.InvalidRequestException;
import com.campusease.exception.ResourceNotFoundException;
import com.campusease.model.User;
import com.campusease.security.JwtUtils;
import com.campusease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Register
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new InvalidRequestException("Username and password must be provided");
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new InvalidRequestException("Username already exists");
        }

        User savedUser = userService.registerUser(user);
        savedUser.setPassword(null); // hide password
        return ResponseEntity.ok(savedUser);
    }

    // Login (SECURE)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        User dbUser = userService.findByUsername(user.getUsername()).get();
        String token = jwtUtils.generateToken(dbUser.getUsername());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", Map.of(
                "id", dbUser.getId(),
                "username", dbUser.getUsername(),
                "email", dbUser.getEmail(),
                "role", "ROLE_" + dbUser.getRole()
            )
        ));
    }


    // Get all users (ADMIN)
    @GetMapping("/all")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update logged-in user (email/password only)
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateRequest req,
                                           Authentication authentication) {

        String username = authentication.getName();

        User existingUser = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (req.getEmail() != null)
            existingUser.setEmail(req.getEmail());

        if (req.getPassword() != null)
            existingUser.setPassword(passwordEncoder.encode(req.getPassword()));

        User savedUser = userService.updateUser(existingUser);
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }


    // Delete logged-in user
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String username = authentication.getName();

        User existingUser = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userService.deleteUserById(existingUser.getId());
        return ResponseEntity.ok("Account deleted successfully");
    }
}
