package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.backend.entity.Complaint;
import com.example.backend.repository.ComplaintRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ComplaintRepository complaintRepository;

	@Test
	void complaintSubmissionIsPublic() throws Exception {
		mockMvc.perform(post("/api/complaints/add")
				.contentType("application/json")
				.content("""
						{"name":"Test Citizen","address":"1 River Road","zone":"North","complaintType":"Water Leakage","phoneNumber":"1234567890","emailAddress":"citizen@example.com"}
						"""))
				.andExpect(status().isOk());
	}

	@Test
	void complaintReadsRequireAdminAuthentication() throws Exception {
		mockMvc.perform(get("/api/complaints/getAll"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/api/complaints/getAll")
				.with(httpBasic("test-admin", "test-admin-password")))
				.andExpect(status().isOk());
	}

	@Test
	void adminCanPersistComplaintStatus() throws Exception {
		Complaint complaint = new Complaint();
		complaint.setName("Test Citizen");
		complaint.setAddress("1 River Road");
		complaint.setZone("North");
		complaint.setComplaintType("Water Leakage");
		complaint.setPhoneNumber("1234567890");
		complaint.setEmailAddress("citizen@example.com");
		complaint = complaintRepository.save(complaint);

		mockMvc.perform(patch("/api/complaints/" + complaint.getId() + "/status")
				.with(httpBasic("test-admin", "test-admin-password"))
				.contentType("application/json")
				.content("{\"status\":\"accepted\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("accepted"));
	}

}
