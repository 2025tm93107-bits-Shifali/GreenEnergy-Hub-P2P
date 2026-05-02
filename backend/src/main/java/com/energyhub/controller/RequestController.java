package com.energyhub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.energyhub.model.EnergyRequest;
import com.energyhub.repository.EnergyRequestRepository;
import com.energyhub.repository.EnergyRepository; // Agar aapki file ka naam EnergyRepository hai

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    private EnergyRequestRepository repository;
    
    @Autowired
    private EnergyRepository energyRepository; // Agar aapki file ka naam EnergyRepository hai


    // POST endpoint to create a new energy request with status 'PENDING'
    @PostMapping
    public EnergyRequest createRequest(@RequestBody EnergyRequest request) {
        request.setStatus("PENDING");
        return repository.save(request);
    }
    
    // PUT endpoint to update request status (ACCEPTED/REJECTED) by ID
    @PutMapping("/{id}/status")
    public EnergyRequest updateRequestStatus(@PathVariable Long id, @RequestParam String status) {
        EnergyRequest request = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        
        request.setStatus(status.toUpperCase());
        EnergyRequest savedRequest = repository.save(request);

        // Ye hai main logic: Auto-Update Listing to SOLD
        if ("ACCEPTED".equalsIgnoreCase(status)) {
            energyRepository.findById(request.getListingId()).ifPresent(listing -> {
                listing.setStatus("SOLD");
                energyRepository.save(listing);
            });
        }
        
        return savedRequest;
    }
    
    // GET endpoint for ADMIN to fetch all energy requests
    @GetMapping
    public List<EnergyRequest> getAllRequests() {
        return repository.findAll();
    }
    
    // GET endpoint to fetch energy requests filtered by status
    @GetMapping("/status/{status}")
    public List<EnergyRequest> getRequestsByStatus(@PathVariable String status) {
        return repository.findByStatus(status.toUpperCase());
    }
}