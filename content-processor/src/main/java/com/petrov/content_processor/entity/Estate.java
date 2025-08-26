package com.petrov.content_processor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "estates",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"cadastr_number", "source"}
		))
public class Estate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "source", nullable = false)
	private String source;

	@NotNull
	@Column(name = "price", nullable = false, precision = 15, scale = 2)
	private BigDecimal price;

	@Column(name = "type")
	private String type;

	@Column(name = "square", precision = 10, scale = 2)
	private BigDecimal square;

	@Column(name = "room_count")
	private Integer roomCount;

	@Column(name = "floor")
	private Integer floor;

	@Column(name = "total_floors")
	private Integer totalFloor;

	@Column(name = "address")
	private String address;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Version
	private Long version;

	public Estate() {
		this.updatedAt = LocalDateTime.now();
	}
}
