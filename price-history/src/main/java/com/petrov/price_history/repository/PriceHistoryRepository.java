package com.petrov.price_history.repository;

import com.petrov.price_history.entity.PriceHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryRecord, Long> {

	List<PriceHistoryRecord> findByCadastrNumberAndSourceOrderByRecordedAtDesc(String cadastrNumber, String source);
}
