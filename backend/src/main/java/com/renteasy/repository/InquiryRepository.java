package com.renteasy.repository;

import com.renteasy.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
	List<Inquiry> findByPropertyIdOrderByCreatedAtDesc(Long propertyId);
}

