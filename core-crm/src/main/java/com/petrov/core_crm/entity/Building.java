package com.petrov.core_crm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "buildings")
public class Building {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cadastr_number", unique = true, nullable = false)
	private String cadastrNumber;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String address;

	@Column(name = "building_type")
	private String buildingType;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
	private List<Task> tasks = new ArrayList<>();

	@PreUpdate
	@PrePersist
	public void updateTimestamps() {
		this.updatedAt = LocalDateTime.now();
		if (this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}

	}
}
