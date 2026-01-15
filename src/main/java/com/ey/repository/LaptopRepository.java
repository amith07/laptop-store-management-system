package com.ey.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Laptop;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {

	boolean existsBySerialNumber(String serialNumber);

	Optional<Laptop> findByIdAndDeletedAtIsNull(Long id);

	Page<Laptop> findAllByDeletedAtIsNull(Pageable pageable);
}
