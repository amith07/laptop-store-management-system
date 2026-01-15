package com.ey.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	boolean existsByNameIgnoreCase(String name);

	Optional<Brand> findByIdAndDeletedAtIsNull(Long id);

	Page<Brand> findAllByDeletedAtIsNull(Pageable pageable);
}
