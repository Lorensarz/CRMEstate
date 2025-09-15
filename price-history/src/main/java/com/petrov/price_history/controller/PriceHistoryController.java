package com.petrov.price_history.controller;

import com.petrov.price_history.entity.PriceHistoryRecord;
import com.petrov.price_history.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
public class PriceHistoryController {

	private final PriceHistoryRepository priceHistoryRepository;

	@GetMapping("/{cadastrNumber}/{source}")
	public ResponseEntity<List<PriceHistoryRecord>> getPriceHistory(
			@PathVariable String cadastrNumber,
			@PathVariable String source,
			@RequestParam(defaultValue = "30") int days) {

		LocalDateTime startDate = LocalDateTime.now().minusDays(days);
		List<PriceHistoryRecord> history = priceHistoryRepository.findHistoryByDateRange(
				cadastrNumber, source, startDate, LocalDateTime.now());

		return ResponseEntity.ok(history);
	}
}