package com.petrov.content_processor.batch;

import com.petrov.content_processor.entity.Estate;
import com.petrov.content_processor.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EstateItemWriter implements ItemWriter<Estate> {

	private final EstateRepository estateRepository;

	@Override
	public void write(Chunk<? extends Estate> chunk) throws Exception {
		List<Estate> estates = chunk.getItems().stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		if (!estates.isEmpty()) {
			estateRepository.saveAll(estates);
			log.info("Successfully saved {} estates to database", estates.size());
		} else {
			log.debug("No estates to save in this chunk");
		}
	}
}