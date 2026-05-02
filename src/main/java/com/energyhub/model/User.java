package com.energyhub.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data // Lombok annotation for getters, setters, toString, etc.
@lombok.NoArgsConstructor // No-args constructor
@lombok.AllArgsConstructor // All-args constructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "wallet_balance")
	private Double walletBalance = 1000.00;

	// Getters and Setters
	public Double getWalletBalance() { return walletBalance; }
	public void setWalletBalance(Double walletBalance) { this.walletBalance = walletBalance; }
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Column(nullable = false)
    private String password;
    
    private String email;
    private String role; // RESIDENT or ADMIN

    // Getters and Setters (Jo aapne pehle banaye wahi rahenge)
}