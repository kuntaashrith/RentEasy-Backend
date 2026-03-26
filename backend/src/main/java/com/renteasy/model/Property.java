package com.renteasy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "properties",
		indexes = {
				@Index(name = "idx_properties_city", columnList = "city"),
				@Index(name = "idx_properties_rent", columnList = "rent"),
				@Index(name = "idx_properties_bhk", columnList = "bhk"),
				@Index(name = "idx_properties_owner", columnList = "owner_id")
		}
)
public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 160)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, length = 80)
	private String city;

	@Column(nullable = false, length = 255)
	private String address;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal rent;

	@Column(nullable = false)
	private Integer bhk;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private PropertyType propertyType;

	@Column(nullable = false)
	private Boolean available;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@PrePersist
	void onCreate() {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
		if (available == null) {
			available = Boolean.TRUE;
		}
	}
}

