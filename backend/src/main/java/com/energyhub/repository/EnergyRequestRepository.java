package com.energyhub.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.energyhub.model.EnergyRequest;


public interface EnergyRequestRepository extends JpaRepository<EnergyRequest, Long> {
    List<EnergyRequest> findByStatus(String status);
}