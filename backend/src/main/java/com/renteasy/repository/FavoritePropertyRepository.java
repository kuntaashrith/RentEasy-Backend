package com.renteasy.repository;

import com.renteasy.model.FavoriteProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePropertyRepository extends JpaRepository<FavoriteProperty, Long> {
	boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

	Optional<FavoriteProperty> findByUserIdAndPropertyId(Long userId, Long propertyId);

	List<FavoriteProperty> findByUserId(Long userId);
}

