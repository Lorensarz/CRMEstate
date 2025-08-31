package com.petrov.price_history.service;

import com.petrov.price_history.dto.EstateDataDto;
import com.petrov.price_history.entity.PriceHistoryRecord;
import com.petrov.price_history.mapper.EstateDataMapper;
import com.petrov.price_history.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceHistoryService {

	private final PriceHistoryRepository priceHistoryRepository;
	private final EstateDataMapper estateDataMapper;



	@Transactional
	public void processEstateBatch(List<EstateDataDto> estateDataDtoList) {
		if (estateDataDtoList.isEmpty()) {
			return;
		}

		List<PriceHistoryRecord> records = estateDataDtoList.stream()
				.map(estateDataMapper::estateDataDtoToPriceHistoryRecord)
				.toList();
		List<PriceHistoryRecord> savedRecords = priceHistoryRepository.saveAll(records);

		log.info("Saved {} price history records to DB. Sample cadastr numbers: {}",
				savedRecords.size(),
				savedRecords.stream()
						.limit(5)
						.map(PriceHistoryRecord::getCadastrNumber)
						.collect(Collectors.toList())
		);
	}

}
