package com.energyhub.controller;

import com.energyhub.model.EnergyListing;
import com.energyhub.repository.EnergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*") // Frontend integration ke liye zaroori hai
public class EnergyController {

	@Autowired
	private EnergyRepository repository;

//create a new energy listing with validation and error handling
	@PostMapping
	public EnergyListing createListing(@RequestBody EnergyListing listing) {
		if (listing.getProducerName() == null || listing.getProducerName().isEmpty()) {
			throw new IllegalArgumentException("Producer name is required");
		}
		if (listing.getEnergyKwh() == null || listing.getEnergyKwh() <= 0) {
			throw new IllegalArgumentException("Energy kWh must be greater than 0");
		}
		if (listing.getPricePerUnit() == null || listing.getPricePerUnit() <= 0) {
			throw new IllegalArgumentException("Price per unit must be greater than 0");
		}
		listing.setStatus("AVAILABLE");
		return repository.save(listing);
	}

	// Update listing status to SOLD by ID
	@PutMapping("/{id}/markAsSold")
	public EnergyListing markAsSold(@PathVariable Long id) {
		EnergyListing listing = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Listing not found with ID: " + id));
		listing.setStatus("SOLD");
		return repository.save(listing);
	}

	// GET endpoint to fetch all energy listings for the community dashboard
	@GetMapping("/community")
	public List<EnergyListing> getCommunityListings() {
		return repository.findAll();
	}

	// GET endpoint to filter energy listings by status (e.g., AVAILABLE or SOLD)
	@GetMapping("/status")
	public List<EnergyListing> getListingsByStatus(@RequestParam String status) {
		return repository.findByStatus(status);
	}

	// PUT endpoint method to update energy amount and price for an existing listing
	// by ID with error handling

	@PutMapping("/{id}")
	public EnergyListing updateListing(@PathVariable Long id, @RequestBody EnergyListing updatedListing) {
		EnergyListing existingListing = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Listing not found with ID: " + id));

		// Validation for Energy Units
		if (updatedListing.getEnergyKwh() != null && updatedListing.getEnergyKwh() > 0) {
			existingListing.setEnergyKwh(updatedListing.getEnergyKwh());
		} else {
			throw new IllegalArgumentException("Energy kWh must be greater than 0");
		}

		// Price update (Copilot might suggest this too)
		if (updatedListing.getPricePerUnit() != null && updatedListing.getPricePerUnit() > 0) {
			existingListing.setPricePerUnit(updatedListing.getPricePerUnit());
		}

		return repository.save(existingListing); // Yeh line zaroori hai!
	}

	// DELETE endpoint method to remove an energy listing from the portal by ID with
	@DeleteMapping("/{id}")
	public String deleteListing(@PathVariable Long id) {
		EnergyListing listing = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Listing not found with ID: " + id));
		repository.delete(listing);
		return "Listing deleted successfully!";
	}
	
	// GET endpoint /api/listings/stats to return total listings count and total energy kwh sum
	public static class ListingStats {
	    private long totalListings;
	    private double totalEnergyKwh;

	    public ListingStats(long totalListings, double totalEnergyKwh) {
	        this.totalListings = totalListings;
	        this.totalEnergyKwh = totalEnergyKwh;
	    }

	    public long getTotalListings() { return totalListings; }
	    public double getTotalEnergyKwh() { return totalEnergyKwh; }
	}
	
	@GetMapping("/stats")
	public ListingStats getListingStats() {
	    long totalListings = repository.count();
	    double totalEnergyKwh = repository.findAll().stream()
	            .mapToDouble(EnergyListing::getEnergyKwh)
	            .sum();
	    return new ListingStats(totalListings, totalEnergyKwh);
	}

}