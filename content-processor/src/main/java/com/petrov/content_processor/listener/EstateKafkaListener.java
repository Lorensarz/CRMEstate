package com.petrov.content_processor.listener;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.service.EstateBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EstateKafkaListener {

	private final EstateBatchService estateBatchService;

	@KafkaListener(
			topics = "${spring.kafka.topic}",
			containerFactory = "kafkaListenerContainerFactory",
			groupId = "${spring.kafka.consumer.group-id}",
			id = "batchProcessor"
	)
	public void listenForBatchProcessing(List<EstateDataDto> batch) {
		try {
			log.info("Received batch of {} messages from Kafka", batch.size());
			estateBatchService.processEstateDataBatchOptimized(batch);
			log.info("Successfully processed batch of {} messages", batch.size());
		} catch (Exception e) {
			log.error("Failed to process Kafka batch", e);
		}
	}
}