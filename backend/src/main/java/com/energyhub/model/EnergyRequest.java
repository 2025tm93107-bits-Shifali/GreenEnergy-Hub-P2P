package com.energyhub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "energy_requests")
@Data // Lombok annotation for getters, setters, toString, etc.
@lombok.NoArgsConstructor // No-args constructor
@lombok.AllArgsConstructor // All-args constructor
@lombok.Builder // Builder pattern ke liye

public class EnergyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Long getListingId() {
		return listingId;
	}
	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private Long buyerId; 
    private Long listingId; 
    private String status; // PENDING, ACCEPTED, REJECTED

    // Getters and Setters (Same as yours)
}