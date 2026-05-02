package com.energyhub.repository;

import com.energyhub.model.EnergyListing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyRepository extends JpaRepository<EnergyListing, Long> {

	List<EnergyListing> findByStatus(String status);
	
}