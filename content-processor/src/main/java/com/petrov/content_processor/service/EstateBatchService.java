package com.petrov.content_processor.service;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import com.petrov.content_processor.mapper.EstateMapper;
import com.petrov.content_processor.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstateBatchService {

	private final EstateRepository estateRepository;
	private final EstateMapper estateMapper;


	@Transactional
	public void processEstateDataBatchOptimized(List<EstateDataDto> batch) {
		if (batch.isEmpty()) {
			return;
		}

		log.info("Processing optimized batch of {} items", batch.size());

		// Группируем по кадастровому номеру и источнику для проверки существования
		Map<String, Map<String, EstateDataDto>> groupedData = new HashMap<>();
		for (EstateDataDto dto : batch) {
			groupedData
					.computeIfAbsent(dto.getCadastrNumber(), k -> new HashMap<>())
					.put(dto.getSource(), dto);
		}

		// Получаем существующие записи одним запросом
		List<Estate> existingEstates = new ArrayList<>();
		for (String cadastrNumber : groupedData.keySet()) {
			existingEstates.addAll(estateRepository.findByCadastrNumber(cadastrNumber));
		}

		// Разделяем на обновления и вставки
		List<Estate> toUpdate = new ArrayList<>();
		List<Estate> toInsert = new ArrayList<>();

		for (Estate existing : existingEstates) {
			EstateDataDto dto = groupedData
					.get(existing.getCadastrNumber())
					.get(existing.getSource());

			if (dto != null) {
				estateMapper.updateEntityFromDto(dto, existing);
				toUpdate.add(existing);
				// Убираем из мапы обработанные
				groupedData.get(existing.getCadastrNumber()).remove(existing.getSource());
			}
		}

		// Оставшиеся - новые записи
		for (Map<String, EstateDataDto> sourceMap : groupedData.values()) {
			for (EstateDataDto dto : sourceMap.values()) {
				toInsert.add(estateMapper.toEntity(dto));
			}
		}

		// Батчевое сохранение
		if (!toUpdate.isEmpty()) {
			estateRepository.saveAll(toUpdate);
			log.info("Updated {} existing estates", toUpdate.size());
		}

		if (!toInsert.isEmpty()) {
			estateRepository.saveAll(toInsert);
			log.info("Inserted {} new estates", toInsert.size());
		}

		log.info("Batch processing completed. Total processed: {}", batch.size());
	}
}