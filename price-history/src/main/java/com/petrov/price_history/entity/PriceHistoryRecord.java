package com.petrov.price_history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@AllArgsConstructor
@Getter
@Setter
public class PriceHistoryRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cadastr_number", nullable = false)
	private String cadastrNumber;

	@Column(nullable = false)
	private String source;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private LocalDateTime recordedAt;

	@Column(precision = 10, scale = 2)
	private BigDecimal square;

	private String type;

	@Version
	private Long version;

	public PriceHistoryRecord() {
		this.recordedAt = LocalDateTime.now();
		this.version = 0L;
	}

}
