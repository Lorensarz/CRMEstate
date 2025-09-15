package com.petrov.content_processor.batch;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import com.petrov.content_processor.mapper.EstateMapper;
import com.petrov.content_processor.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EstateItemProcessor implements ItemProcessor<EstateDataDto, Estate> {

	private final EstateRepository estateRepository;
	private final EstateMapper estateMapper;

	@Override
	public Estate process(EstateDataDto dto) throws Exception {
		try {
			Optional<Estate> existingEstate = estateRepository
					.findByCadastrNumberAndSource(dto.getCadastrNumber(), dto.getSource());

			if (existingEstate.isPresent()) {
				Estate estate = existingEstate.get();
				estateMapper.updateEntityFromDto(dto, estate);
				return estate;
			} else {
				return estateMapper.toEntity(dto);
			}
		} catch (Exception e) {
			log.error("Failed to process estate data: {}", dto.getCadastrNumber(), e);
			throw new EstateProcessingException("Failed to process estate: " + dto.getCadastrNumber(), e);
		}
	}
}