package com.example.backend.controller;

import com.example.backend.entity.Complaint;
import com.example.backend.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/add")
    public ResponseEntity<Complaint> createComplaint(@Valid @RequestBody Complaint complaint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.createComplaint(complaint));
    }

    @GetMapping("/getAll")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @PutMapping("/{id}")
    public Complaint updateComplaint(@PathVariable Long id, @Valid @RequestBody Complaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    @PatchMapping("/{id}/status")
    public Complaint updateComplaintStatus(@PathVariable Long id, @RequestBody Map<String, String> update) {
        return complaintService.updateComplaintStatus(id, update.get("status"));
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }
}
