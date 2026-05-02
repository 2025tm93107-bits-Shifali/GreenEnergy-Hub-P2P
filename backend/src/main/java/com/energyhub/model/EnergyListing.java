package com.energyhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "energy_listings")
public class EnergyListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producer_name") // 👈 Explicit mapping
    private String producerName;

    @Column(name = "energy_kwh")    // 👈 Yeh sabse important hai
    private Double energyKwh;

    @Column(name = "price_per_unit") // 👈 Yeh bhi map karein
    private Double pricePerUnit;

    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public Double getEnergyKwh() {
		return energyKwh;
	}

	public void setEnergyKwh(Double energyKwh) {
		this.energyKwh = energyKwh;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    // Getters and Setters... (Baaki sab same rahega)
}