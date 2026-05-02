package com.energyhub.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.energyhub.model.User;
import com.energyhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


//Create a simple Login API that takes username and password, checks it in UserRepository, and returns the User object if successful or 401 Unauthorized if failed. Also create a Signup API.
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@NoArgsConstructor
public class AuthController {
	
	// Inject UserRepository
	@Autowired
	private UserRepository userRepository;

	// POST endpoint for user signup
	@PostMapping("/signup")
	public User signup(@RequestBody User user) {
		// Check if username already exists
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}
		// Save the new user to the database
		return userRepository.save(user);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
	    Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

	    if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
	        User user = userOpt.get();
	        
	        // Custom Response Map banana taaki frontend ko wahi mile jo zaroori hai
	        Map<String, Object> response = new HashMap<>();
	        response.put("username", user.getUsername());
	        response.put("role", user.getRole());
	        response.put("walletBalance", user.getWalletBalance()); // 💳 Wallet balance inclusion
	        
	        return ResponseEntity.ok(response);
	    } else {
	        // Agar credentials galat hain
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(Map.of("message", "Invalid username or password"));
	    }
	}
	// DTO for login request
	public static class LoginRequest {
	    private String username;
	    private String password;

	    public String getUsername() { return username; }
	    public void setUsername(String username) { this.username = username; }
	    public String getPassword() { return password; }
	    public void setPassword(String password) { this.password = password; }
	}
}
