package com.campusease.service;

import com.campusease.model.Complaint;
import com.campusease.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    // Create a new complaint
    public Complaint createComplaint(Complaint complaint) {
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setStatus("Pending");
        return complaintRepository.save(complaint);
    }

    // Get all complaints for a specific user
    public List<Complaint> getComplaintsByUser(String userId) {
        return complaintRepository.findByUserId(userId);
    }
    
    
    public Optional<Complaint> getComplaintById(String id) {
        return complaintRepository.findById(id);
    }

    

    // Get all complaints (for admin or analytics)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Update status (e.g., resolve complaint)
    public Complaint updateComplaintStatus(String id, String status) {
        Complaint complaint = complaintRepository.findById(id).orElse(null);
        if (complaint != null) {
            complaint.setStatus(status);
            return complaintRepository.save(complaint);
        }
        return null;
    }
    public Complaint updateComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    
    public void deleteComplaintById(String id) {
        complaintRepository.deleteById(id);
    }

}
