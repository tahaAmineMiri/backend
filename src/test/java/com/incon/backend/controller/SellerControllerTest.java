//package com.incon.backend.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.incon.backend.dto.request.SellerRequest;
//import com.incon.backend.dto.response.SellerResponse;
//import com.incon.backend.enums.Role;
//import com.incon.backend.service.SellerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SellerControllerTest {
//
//    @Mock
//    private SellerService sellerService;
//
//    @InjectMocks
//    private SellerController sellerController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//    private SellerRequest sellerRequest;
//    private SellerResponse sellerResponse;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(sellerController)
//                .setControllerAdvice(new ResponseEntityExceptionHandler() {})
//                .build();
//        objectMapper = new ObjectMapper();
//
//        sellerRequest = new SellerRequest();
//        sellerRequest.setFullName("Test Seller");
//        sellerRequest.setEmail("test@example.com");
//        sellerRequest.setPassword("password123"); // Add valid password
//        sellerRequest.setPosition("Manager"); // Add valid position
//        sellerRequest.setBusinessPhone("1234567890");
//        sellerRequest.setRole(Role.SELLER); // Add valid role
//
//        sellerResponse = new SellerResponse();
//        sellerResponse.setId(1);
//        sellerResponse.setFullName("Test Seller");
//        sellerResponse.setEmail("test@example.com");
//        sellerResponse.setBusinessPhone("1234567890");
//    }
//
//    @Test
//    public void testRegisterSeller() throws Exception {
//        when(sellerService.registerSeller(any(SellerRequest.class))).thenReturn(sellerResponse);
//
//        mockMvc.perform(post("/api/sellers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sellerRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.fullName").value("Test Seller"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        verify(sellerService, times(1)).registerSeller(any(SellerRequest.class));
//    }
//
//    @Test
//    public void testGetSellerById() throws Exception {
//        when(sellerService.getSellerById(1)).thenReturn(sellerResponse);
//
//        mockMvc.perform(get("/api/sellers/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.fullName").value("Test Seller"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        verify(sellerService, times(1)).getSellerById(1);
//    }
//
//    @Test
//    public void testGetSellerByEmail() throws Exception {
//        when(sellerService.getSellerByEmail("test@example.com")).thenReturn(sellerResponse);
//
//        mockMvc.perform(get("/api/sellers/email/test@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.fullName").value("Test Seller"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        verify(sellerService, times(1)).getSellerByEmail("test@example.com");
//    }
//
//    @Test
//    public void testUpdateSeller() throws Exception {
//        when(sellerService.updateSeller(eq(1), any(SellerRequest.class))).thenReturn(sellerResponse);
//
//        mockMvc.perform(put("/api/sellers/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sellerRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.fullName").value("Test Seller"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        verify(sellerService, times(1)).updateSeller(eq(1), any(SellerRequest.class));
//    }
//
//    @Test
//    public void testRegisterSeller_ValidationFailure() throws Exception {
//        SellerRequest invalidRequest = new SellerRequest();
//
//        mockMvc.perform(post("/api/sellers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest());
//
//        verify(sellerService, never()).registerSeller(any(SellerRequest.class));
//    }
//
//    @Test
//    public void testUpdateSeller_ValidationFailure() throws Exception {
//        SellerRequest invalidRequest = new SellerRequest();
//
//        mockMvc.perform(put("/api/sellers/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest());
//
//        verify(sellerService, never()).updateSeller(eq(1), any(SellerRequest.class));
//    }
//}