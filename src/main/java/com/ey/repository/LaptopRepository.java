package com.ey.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ey.model.Laptop;

public interface LaptopRepository extends JpaRepository<Laptop, Long>, JpaSpecificationExecutor<Laptop> {

	boolean existsBySerialNumber(String serialNumber);

	Optional<Laptop> findByIdAndDeletedAtIsNull(Long id);
}
