package com.petrov.content_processor.service;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import com.petrov.content_processor.mapper.EstateDataMapper;
import com.petrov.content_processor.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstateService {

	private final EstateRepository estateRepository;
	private final EstateDataMapper estateDataMapper;

	@Value("${spring.service.batch-size}")
	private Integer batchSize;

	@Transactional
	public void processEstateBatch(List<EstateDataDto> estateDataDtoList) {
		List<Estate> estates = estateDataDtoList.stream()
				.map(estateDataMapper::estateDataDtoToEstate)
				.toList();

		for (Estate estate : estates) {
			estateRepository.upsertEstate(estate);
		}
	}

	public int getBatchSize() {
		return 1000;
	}

}
