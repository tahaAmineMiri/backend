package com.incon.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.SellerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(scripts = "/test-data.sql") // Optional: Load test data
public class SellerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    public void testRegisterSeller() throws Exception {
        SellerRequest sellerRequest = new SellerRequest();
        sellerRequest.setEmail("seller1@example.com"); // Changed email
        sellerRequest.setPassword("password123");
        sellerRequest.setFullName("Test Seller");
        sellerRequest.setPosition("Manager");
        sellerRequest.setBusinessPhone("1234567890");
        sellerRequest.setRole(com.incon.backend.enums.Role.SELLER);

        mockMvc.perform(post("/api/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sellerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("seller1@example.com"));
    }

    @Test
    public void testGetSellerById() throws Exception {
        SellerRequest sellerRequest = new SellerRequest();
        sellerRequest.setEmail("seller2@example.com"); // Different email
        sellerRequest.setPassword("password123");
        sellerRequest.setFullName("Test Seller");
        sellerRequest.setPosition("Manager");
        sellerRequest.setBusinessPhone("1234567890");
        sellerRequest.setRole(com.incon.backend.enums.Role.SELLER);

        String responseContent = mockMvc.perform(post("/api/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sellerRequest)))
                .andExpect(status().isCreated())
                .andReturn()-
                .getResponse()
                .getContentAsString();

        SellerResponse createdSeller = objectMapper.readValue(responseContent, SellerResponse.class);

        mockMvc.perform(get("/api/sellers/" + createdSeller.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("seller2@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test Seller"));
    }
}