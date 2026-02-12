package com.campusease.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.campusease.model.Complaint;
import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    List<Complaint> findByUserId(String userId);
}
