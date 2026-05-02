package com.energyhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.energyhub.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
}