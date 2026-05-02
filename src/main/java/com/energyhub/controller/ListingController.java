package com.energyhub.controller;


import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.energyhub.model.User;
import com.energyhub.repository.UserRepository;
import com.energyhub.model.EnergyListing; // 👈 Listing model import karna zaroori hai
import com.energyhub.repository.EnergyRepository; // 👈 Listing repository import karna zaroori hai


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class ListingController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EnergyRepository listingRepository; // 👈 Isse add karna zaroori hai
	
	@Transactional
	@PostMapping("/deduct-wallet")
	public ResponseEntity<?> deductWallet(@RequestBody Map<String, Object> request) {
	    try {
	        String username = (String) request.get("username");
	        // Frontend se "id" bhej rahe hain hum Marketplace.js mein
	        Long listingId = Long.valueOf(request.get("listingId").toString()); 
	        Double purchasedAmount = Double.valueOf(request.get("amount").toString());
	        Double totalCost = Double.valueOf(request.get("totalCost").toString());

	        System.out.println("Processing: User=" + username + ", Listing=" + listingId + ", Units=" + purchasedAmount);

	        User user = userRepository.findByUsername(username)
	                     .orElseThrow(() -> new RuntimeException("User not found"));

	        EnergyListing listing = listingRepository.findById(listingId)
	                     .orElseThrow(() -> new RuntimeException("Listing ID " + listingId + " not found in DB"));

	        // Step 1: Deduct Wallet
	        user.setWalletBalance(user.getWalletBalance() - totalCost);
	        userRepository.save(user);

	        // Step 2: Update Inventory
	        double remainingEnergy = listing.getEnergyKwh() - purchasedAmount;
	        System.out.println("Current Energy: " + listing.getEnergyKwh() + ", Remaining: " + remainingEnergy);

	        if (remainingEnergy <= 0) {
	            listingRepository.delete(listing);
	            System.out.println("Listing deleted as energy reached 0");
	        } else {
	            listing.setEnergyKwh(remainingEnergy);
	            listingRepository.save(listing); // 👈 Ye line database update karegi
	            System.out.println("Listing updated in database");
	        }

	        return ResponseEntity.ok(Map.of("newBalance", user.getWalletBalance()));

	    } catch (Exception e) {
	        e.printStackTrace(); // 👈 STS Console mein error check karein
	        return ResponseEntity.status(500).body("Error: " + e.getMessage());
	    }
	}
	
	// ListingController.java ke andar ye add karein:

	@PostMapping("/add-listing")
	public ResponseEntity<?> addListing(@RequestBody Map<String, Object> request) {
	    try {
	        EnergyListing newListing = new EnergyListing();
	        
	        // Frontend se data map karna
	        newListing.setProducerName((String) request.get("producerName"));
	        newListing.setEnergyKwh(Double.valueOf(request.get("energyKwh").toString()));
	        newListing.setPricePerUnit(Double.valueOf(request.get("pricePerUnit").toString()));
	        newListing.setStatus("AVAILABLE"); // Default status

	        listingRepository.save(newListing); // Database mein save
	        
	        return ResponseEntity.ok(Map.of("message", "Listing added successfully!"));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error adding listing: " + e.getMessage());
	    }
	}
	
	// ListingController.java mein ye methods add karein:

	// 1. DELETE: Listing ko remove karne ke liye
	@DeleteMapping("/delete-listing/{id}")
	@Transactional
	public ResponseEntity<?> deleteListing(@PathVariable Long id) {
	    try {
	        listingRepository.deleteById(id);
	        return ResponseEntity.ok(Map.of("message", "Listing deleted successfully"));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error deleting listing");
	    }
	}

	// 2. UPDATE: Listing ki details change karne ke liye
	@PutMapping("/update-listing/{id}")
	@Transactional
	public ResponseEntity<?> updateListing(@PathVariable Long id, @RequestBody Map<String, Object> request) {
	    try {
	        EnergyListing listing = listingRepository.findById(id)
	                     .orElseThrow(() -> new RuntimeException("Listing not found"));
	        
	        listing.setEnergyKwh(Double.valueOf(request.get("energyKwh").toString()));
	        listing.setPricePerUnit(Double.valueOf(request.get("pricePerUnit").toString()));
	        
	        listingRepository.save(listing);
	        return ResponseEntity.ok(Map.of("message", "Listing updated successfully"));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error updating listing");
	    }
	}
}