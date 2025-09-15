package com.petrov.content_processor.service;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import com.petrov.content_processor.mapper.EstateMapper;
import com.petrov.content_processor.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstateService {

	private final EstateRepository estateRepository;
	private final EstateMapper estateMapper;

	/**
	 * Обработка одного объекта недвижимости
	 */
	@Transactional
	public void processEstateData(EstateDataDto dto) {
		try {
			Optional<Estate> existingEstate = estateRepository
					.findByCadastrNumberAndSource(dto.getCadastrNumber(), dto.getSource());

			if (existingEstate.isPresent()) {
				// Обновляем существующую запись
				Estate estate = existingEstate.get();
				estateMapper.updateEntityFromDto(dto, estate);
				estateRepository.save(estate);
				log.debug("Updated estate: {}", dto.getCadastrNumber());
			} else {
				// Создаем новую запись
				Estate newEstate = estateMapper.toEntity(dto);
				estateRepository.save(newEstate);
				log.debug("Created new estate: {}", dto.getCadastrNumber());
			}
		} catch (Exception e) {
			log.error("Error processing estate data: {}", dto.getCadastrNumber(), e);
			throw new RuntimeException("Failed to process estate data", e);
		}
	}

	/**
	 * Батчевая обработка объектов недвижимости
	 */
	@Transactional
	public void processEstateDataBatch(List<EstateDataDto> batch) {
		try {
			log.info("Processing batch of {} estate items", batch.size());

			for (EstateDataDto dto : batch) {
				processEstateData(dto);
			}

			log.info("Successfully processed batch of {} items", batch.size());
		} catch (Exception e) {
			log.error("Error processing estate batch", e);
			throw new RuntimeException("Failed to process estate batch", e);
		}
	}

	/**
	 * Получение всех объектов недвижимости по кадастровому номеру
	 */
	@Transactional(readOnly = true)
	public List<Estate> getEstatesByCadastrNumber(String cadastrNumber) {
		return estateRepository.findByCadastrNumber(cadastrNumber);
	}

	/**
	 * Получение объекта недвижимости по кадастровому номеру и источнику
	 */
	@Transactional(readOnly = true)
	public Optional<Estate> getEstateByCadastrNumberAndSource(String cadastrNumber, String source) {
		return estateRepository.findByCadastrNumberAndSource(cadastrNumber, source);
	}
}