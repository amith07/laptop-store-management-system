package com.ey.repository.spec;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ey.enums.LaptopStatus;
import com.ey.model.Laptop;

import jakarta.persistence.criteria.Predicate;

public class LaptopSpecification {

	public static Specification<Laptop> build(Long brandId, String cpu, BigDecimal minPrice, BigDecimal maxPrice,
			LaptopStatus status) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			// Exclude soft deleted
			predicates.add(cb.isNull(root.get("deletedAt")));

			if (brandId != null) {
				predicates.add(cb.equal(root.get("brand").get("id"), brandId));
			}

			if (cpu != null && !cpu.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("cpu")), "%" + cpu.toLowerCase() + "%"));
			}

			if (minPrice != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("effectivePrice"), minPrice));
			}

			if (maxPrice != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("effectivePrice"), maxPrice));
			}

			if (status != null) {
				predicates.add(cb.equal(root.get("status"), status));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
