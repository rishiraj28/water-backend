package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private ObjectMapper objectMapper;

	@Test
	void complaintSubmissionIsPublic() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/complaints/add")
				.contentType("application/json")
				.content("""
						{"name":"Test Citizen","address":"1 River Road","zone":"North","complaintType":"Water Leakage","phoneNumber":"1234567890","emailAddress":"citizen@example.com","description":"Water is leaking near the road."}
						"""))
				.andExpect(status().isCreated())
						.andExpect(jsonPath("$.description").value("Water is leaking near the road."))
						.andExpect(jsonPath("$.id").isNumber())
						.andReturn();

				long savedId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
				mockMvc.perform(get("/api/complaints/getAll")
						.with(httpBasic("test-admin", "test-admin-password")))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[*].id").value(org.hamcrest.Matchers.hasItem((int) savedId)))
						.andExpect(jsonPath("$[*].description").value(org.hamcrest.Matchers.hasItem("Water is leaking near the road.")));
	}

	@Test
	void complaintSubmissionRejectsInvalidData() throws Exception {
		mockMvc.perform(post("/api/complaints/add")
				.contentType("application/json")
				.content("{\"name\":\"\"}"))
				.andExpect(status().isBadRequest());
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
		MvcResult created = mockMvc.perform(post("/api/complaints/add")
				.contentType("application/json")
				.content("""
						{"name":"Status Test","address":"2 River Road","zone":"South","complaintType":"Low Water Pressure","phoneNumber":"1234567890","emailAddress":"status@example.com","description":"Pressure is low."}
						"""))
				.andExpect(status().isCreated())
				.andReturn();
		long complaintId = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

		mockMvc.perform(patch("/api/complaints/" + complaintId + "/status")
				.with(httpBasic("test-admin", "test-admin-password"))
				.contentType("application/json")
				.content("{\"status\":\"in_progress\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("in_progress"));

		mockMvc.perform(get("/api/complaints/" + complaintId)
				.with(httpBasic("test-admin", "test-admin-password")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("in_progress"));
	}

}
