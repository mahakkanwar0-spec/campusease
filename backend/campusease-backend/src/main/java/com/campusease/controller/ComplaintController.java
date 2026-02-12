package com.campusease.controller;

import com.campusease.dto.UpdateComplaintRequest;
import com.campusease.exception.InvalidRequestException;
import com.campusease.exception.ResourceNotFoundException;
import com.campusease.model.Complaint;
import com.campusease.model.User;
import com.campusease.repository.UserRepository;
import com.campusease.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserRepository userRepository;

    // CREATE complaint
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Complaint> createComplaint(
            @RequestBody Complaint complaint,
            Authentication authentication) {

        if (authentication == null) {
            throw new InvalidRequestException("Unauthorized");
        }

        if (complaint.getTitle() == null || complaint.getTitle().isBlank()) {
            throw new InvalidRequestException("Title must be provided");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        complaint.setUserId(user.getId());
        complaint.setTitle(complaint.getTitle().trim());

        if (complaint.getDescription() != null) {
            complaint.setDescription(complaint.getDescription().trim());
        }

        return ResponseEntity.ok(complaintService.createComplaint(complaint));
    }

    // GET complaints by user
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getComplaintsByUser(
            @PathVariable String userId) {

        List<Complaint> complaints = complaintService.getComplaintsByUser(userId);

        if (complaints.isEmpty()) {
            throw new ResourceNotFoundException("No complaints found");
        }

        return ResponseEntity.ok(complaints);
    }

    // GET complaint by id
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable String id) {
        return ResponseEntity.ok(
                complaintService.getComplaintById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Complaint not found"))
        );
    }

    // UPDATE complaint (OWNER)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable String id,
            @RequestBody UpdateComplaintRequest req,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Complaint existing = complaintService.getComplaintById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        if (!existing.getUserId().equals(user.getId())) {
            throw new InvalidRequestException("Not authorized");
        }

        if (req.getTitle() != null)
            existing.setTitle(req.getTitle().trim());

        if (req.getDescription() != null)
            existing.setDescription(req.getDescription().trim());

        return ResponseEntity.ok(complaintService.updateComplaint(existing));
    }

    // UPDATE STATUS (ADMIN ONLY) 🔥 FIXED
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");

        if (status == null || status.isBlank()) {
            throw new InvalidRequestException("Status must be provided");
        }

        Complaint updated =
                complaintService.updateComplaintStatus(id, status.trim());

        return ResponseEntity.ok(updated);
    }

    // DELETE complaint (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        complaintService.deleteComplaintById(id);
        return ResponseEntity.ok("Deleted");
    }

    // GET ALL (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();



        return ResponseEntity.ok(complaints);
    }
}
