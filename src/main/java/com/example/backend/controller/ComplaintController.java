package com.example.backend.controller;

import com.example.backend.entity.Complaint;
import com.example.backend.service.ComplaintService;
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
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    @GetMapping("/getAll")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable String id) {
        return complaintService.getComplaintById(id);
    }

    @PutMapping("/{id}")
    public Complaint updateComplaint(@PathVariable String id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    @PatchMapping("/{id}/status")
    public Complaint updateComplaintStatus(@PathVariable String id, @RequestBody Map<String, String> update) {
        return complaintService.updateComplaintStatus(id, update.get("status"));
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable String id) {
        complaintService.deleteComplaint(id);
    }
}
