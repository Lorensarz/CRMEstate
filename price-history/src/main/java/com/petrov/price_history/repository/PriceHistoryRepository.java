package com.petrov.price_history.repository;

import com.petrov.price_history.entity.PriceHistoryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryRecord, Long> {

	List<PriceHistoryRecord> findByCadastrNumberAndSourceOrderByRecordedAtDesc(
			String cadastrNumber, String source);

	Page<PriceHistoryRecord> findByCadastrNumberAndSource(
			String cadastrNumber, String source, Pageable pageable);

	@Query("SELECT p FROM PriceHistoryRecord p WHERE p.cadastrNumber = :cadastrNumber " +
			"AND p.source = :source AND p.recordedAt BETWEEN :startDate AND :endDate " +
			"ORDER BY p.recordedAt DESC")
	List<PriceHistoryRecord> findHistoryByDateRange(
			@Param("cadastrNumber") String cadastrNumber,
			@Param("source") String source,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
}