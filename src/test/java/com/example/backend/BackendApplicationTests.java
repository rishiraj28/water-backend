package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.backend.config.CorsConfig;
import com.example.backend.config.SecurityConfig;
import com.example.backend.controller.AdminController;
import com.example.backend.controller.ComplaintController;
import com.example.backend.entity.Complaint;
import com.example.backend.service.ComplaintService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { ComplaintController.class, AdminController.class })
@Import({ SecurityConfig.class, CorsConfig.class })
@TestPropertySource(properties = {
		"app.admin.username=test-admin",
		"app.admin.password=test-admin-password",
		"app.cors.allowed-origins=http://localhost:3000"
})
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ComplaintService complaintService;

	@Test
	void complaintSubmissionIsPublic() throws Exception {
		when(complaintService.createComplaint(any(Complaint.class))).thenReturn(complaint("pending"));

		mockMvc.perform(post("/api/complaints/add")
				.contentType("application/json")
				.content("""
						{"name":"Test Citizen","address":"1 River Road","zone":"North","complaintType":"Water Leakage","phoneNumber":"1234567890","emailAddress":"citizen@example.com"}
						"""))
				.andExpect(status().isOk());
	}

	@Test
	void complaintReadsRequireAdminAuthentication() throws Exception {
		when(complaintService.getAllComplaints()).thenReturn(List.of());

		mockMvc.perform(get("/api/complaints/getAll"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/api/complaints/getAll")
				.with(httpBasic("test-admin", "test-admin-password")))
				.andExpect(status().isOk());
	}

	@Test
	void adminCanPersistComplaintStatus() throws Exception {
		when(complaintService.updateComplaintStatus("sample-id", "accepted"))
				.thenReturn(complaint("accepted"));

		mockMvc.perform(patch("/api/complaints/sample-id/status")
				.with(httpBasic("test-admin", "test-admin-password"))
				.contentType("application/json")
				.content("{\"status\":\"accepted\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("accepted"));
	}

	private Complaint complaint(String status) {
		Complaint complaint = new Complaint();
		complaint.setId("sample-id");
		complaint.setName("Test Citizen");
		complaint.setAddress("1 River Road");
		complaint.setZone("North");
		complaint.setComplaintType("Water Leakage");
		complaint.setPhoneNumber("1234567890");
		complaint.setEmailAddress("citizen@example.com");
		complaint.setStatus(status);
		return complaint;
	}
}
