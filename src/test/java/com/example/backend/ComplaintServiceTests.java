package com.example.backend;

import com.example.backend.entity.Complaint;
import com.example.backend.repository.ComplaintRepository;
import com.example.backend.service.ComplaintService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTests {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void statusUpdateSavesTheMongoDocument() {
        Complaint complaint = new Complaint();
        when(complaintRepository.findById("sample-id")).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        Complaint updated = complaintService.updateComplaintStatus("sample-id", "accepted");

        assertEquals("accepted", updated.getStatus());
        verify(complaintRepository).save(complaint);
    }

    @Test
    void invalidStatusIsRejectedBeforeAccessingMongo() {
        assertThrows(ResponseStatusException.class,
                () -> complaintService.updateComplaintStatus("sample-id", "unknown"));

        verifyNoInteractions(complaintRepository);
    }
}