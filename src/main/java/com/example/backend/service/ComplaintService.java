package com.example.backend.service;

import com.example.backend.entity.Complaint;
import com.example.backend.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Complaint not found with id: " + id));
    }

    public Complaint updateComplaint(Long id, Complaint complaint) {
        complaint.setId(id);
        return complaintRepository.save(complaint);
    }

    public Complaint updateComplaintStatus(Long id, String status) {
        if (status == null || !List.of("pending", "in_progress", "resolved").contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid complaint status");
        }

        Complaint complaint = getComplaintById(id);
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }
}
