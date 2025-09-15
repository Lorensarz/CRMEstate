package com.petrov.content_processor.service;

import com.petrov.content_processor.batch.EstateItemReader;
import com.petrov.content_processor.dto.EstateDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueManagementService {

	private final EstateItemReader estateItemReader;
	private final EstateService estateService;

	@Scheduled(fixedDelay = 30000) // Каждые 30 секунд
	public void processQueueIfNotEmpty() {
		int queueSize = estateItemReader.getQueueSize();
		if (queueSize > 0) {
			log.info("Queue has {} items, processing...", queueSize);
		}
	}

	public void addToQueue(List<EstateDataDto> items) {
		estateItemReader.addItems(items);
		log.info("Added {} items to processing queue", items.size());
	}

	public int getQueueSize() {
		return estateItemReader.getQueueSize();
	}

	public void clearQueue() {
		estateItemReader.clearQueue();
		log.info("Processing queue cleared");
	}
}
