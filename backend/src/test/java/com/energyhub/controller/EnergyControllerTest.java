package com.energyhub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.energyhub.controller.EnergyController;
import com.energyhub.model.EnergyListing;
import com.energyhub.repository.EnergyRepository;

@WebMvcTest(EnergyController.class) // Sirf EnergyController ko load karega test ke liye
public class EnergyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Repository ko mock karne ke liye
    private EnergyRepository repository; // Controller repository use kar raha hai toh ise mock karna hoga

    @Test // Yeh annotation zaroori hai test recognize karne ke liye
    public void testCreateListing_ValidData() throws Exception {
        EnergyListing mockListing = new EnergyListing();
        mockListing.setId(1L);
        mockListing.setProducerName("Test Producer");
        mockListing.setEnergyKwh(100.0);
        mockListing.setPricePerUnit(0.15);
        mockListing.setStatus("AVAILABLE");

        // Repository save ko mock karein
        when(repository.save(any(EnergyListing.class))).thenReturn(mockListing);

        mockMvc.perform(post("/api/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"producerName\":\"Test Producer\",\"energyKwh\":100.0,\"pricePerUnit\":0.15}"))
                .andExpect(status().isOk()) // Controller 'return repository.save' karta hai jo default 200 OK deta hai
                .andExpect(jsonPath("$.producerName").value("Test Producer"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }
}
